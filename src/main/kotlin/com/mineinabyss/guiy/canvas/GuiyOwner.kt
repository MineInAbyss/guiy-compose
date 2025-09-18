package com.mineinabyss.guiy.canvas

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.components.canvases.InventoryHolder
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.modifiers.Constraints
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.navigation.BackGestureDispatcher
import com.mineinabyss.guiy.navigation.LocalBackGestureDispatcher
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.viewmodel.GuiyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KType

@GuiyUIScopeMarker
class GuiyOwner(
    initialViewers: Set<Player> = setOf(),
) : CoroutineScope {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }

    // We use our own GuiyUIDispatcher which acts like an Dispatchers.Main.immediate
    // Immediate is important for compose to correctly finish recompositions in one cycle (it does operations that will yield several times otherwise.)
    //TODO come up with a test that breaks when not using an immediate style dispatcher.
    val composeScope = CoroutineScope(GuiyUIDispatcher.Main + clock)

    private val _viewers: MutableStateFlow<Set<Player>> = MutableStateFlow(initialViewers)
    val viewers = _viewers.asStateFlow()

    //    val mainThreadScope = CoroutineScope(guiyPlugin.minecraftDispatcher) + SupervisorJob()
    private val viewModels = mutableMapOf<KType, GuiyViewModel>()
    override val coroutineContext: CoroutineContext = composeScope.coroutineContext

    private val rootNode = LayoutNode()

    var running = false
    private val recomposer = Recomposer(coroutineContext)
    private val composition = Composition(GuiyNodeApplier(rootNode), recomposer)

    var applyScheduled = false
    //FIXME I think this will set applyScheduled to true for ALL GuiyOwner instances when any of them update?
    val snapshotHandle = Snapshot.registerGlobalWriteObserver {
        if (!applyScheduled) {
            applyScheduled = true
            composeScope.launch {
                applyScheduled = false
                Snapshot.sendApplyNotifications()
            }
        }
    }

    var exitScheduled = false

    /** Stops sending UI updates to these Players. If no players remain, the composition automatically exits. */
    fun removeViewers(vararg viewers: Player) {
        _viewers.update { it - viewers }
        if (_viewers.value.isEmpty()) {
            exit()
        }
    }

    fun exit() {
        exitScheduled = true
    }

    fun start(content: @Composable () -> Unit) {
        !running || return // Prevent starting twice
        running = true

        launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        // TODO we want better detection of when a player closes an inventory in a way we didn't track,
        //  ex. when a plugin closes it for them, they disconnect, or any other edge cases.
        //  This gets complicated as we might be managing multiple Inventory instances (ex. moving player from Chest -> Anvil, or in the future, dialogs)
        // In the meantime, we just remove offline players from viewers to prevent creeping new snapshots in most cases.
        launch {
            while (!exitScheduled) {
                _viewers.value.forEach {
                    if (!it.isOnline) removeViewers(it)
                }
                delay(1000)
            }
        }

        launch {
            setContent(content)
            while (!exitScheduled) {
                if (hasFrameWaiters) {
                    hasFrameWaiters = false
                    //FIXME I believe this should be called every iteration but we get odd behavior if placed
                    // below this block. Read up on it and try to mimic Mosaic's updated approach in the future.
                    clock.sendFrame(System.nanoTime()) // Send current time (for calculating dT in compose animations)
                    rootNode.measure(Constraints())
                    rootNode.render()
                }
                // UI is independent of the main thread in Minecraft, however since this is still sending updates
                // as packets over the network, and inventory updates will instantly send update packets, we don't want
                // to hammer any connection, so we keep this reasonably high.
                delay(10)
            }
            // FIXME handle exiting correctly when while loop errors, i.e. by switching to a try/finally.
            running = false
            recomposer.close()
            snapshotHandle.dispose()
            composition.dispose()
            viewModels.values.forEach { it.close() }
            composeScope.cancel()
        }
    }

    fun <T> getViewModel(type: KType): T? {
        return viewModels[type] as? T
    }

    fun addViewModel(type: KType, viewModel: GuiyViewModel) {
        viewModels[type] = viewModel
    }


    private fun setContent(content: @Composable () -> Unit) {
        hasFrameWaiters = true
        composition.setContent {
            CompositionLocalProvider(
                LocalGuiyOwner provides this,
                LocalBackGestureDispatcher provides BackGestureDispatcher(),
                LocalClickHandler provides object : ClickHandler {
                    override fun processClick(scope: ClickScope): ClickResult {
                        return rootNode.children.fold(ClickResult()) { acc, node ->
                            val processed = rootNode.processClick(scope)
                            if (processed.clickConsumed == true) return processed
                            acc.mergeWith(processed)
                        }
                    }
                }) {
                // A default inventory holder for most usecases
                InventoryHolder {
                    content()
                }
            }
        }
    }
}

fun guiy(
    vararg initialViewers: Player,
    content: @Composable () -> Unit,
): GuiyOwner {
    return GuiyOwner(initialViewers.toSet()).apply {
        start {
            content()
        }
    }
}
