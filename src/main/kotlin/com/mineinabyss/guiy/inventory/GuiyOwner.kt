package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.modifiers.Constraints
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.modifiers.drag.DragScope
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.bukkit.entity.Player
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KType
import kotlin.reflect.typeOf

data class ClickResult(
    val cancelBukkitEvent: Boolean? = null,
) {
    fun mergeWith(other: ClickResult) = ClickResult(
        // Prioritize true > false > null
        cancelBukkitEvent = (cancelBukkitEvent ?: other.cancelBukkitEvent)?.or(other.cancelBukkitEvent ?: false)
    )
}

interface ClickHandler {
    fun processClick(scope: ClickScope): ClickResult
    fun processDrag(scope: DragScope)
}

@GuiyUIScopeMarker
class GuiyOwner(
    initialViewers: Set<Player> = setOf(),
) : CoroutineScope {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }
    val composeScope = CoroutineScope(guiyPlugin.asyncDispatcher) + clock
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

    fun removeViewers(vararg viewers: Player) {
        _viewers.update { it - viewers }
        if(_viewers.value.isEmpty()) {
            exit()
        }
    }

    fun exit() {
        exitScheduled = true
    }

    fun <T> getViewModel(type: KType): T? {
        return viewModels[type] as? T
    }

    fun addViewModel(type: KType, viewModel: GuiyViewModel) {
        viewModels[type] = viewModel
    }

    fun start(content: @Composable () -> Unit) {
        !running || return
        running = true

        launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        launch {
            setContent(content)
            while (!exitScheduled) {
                //            Bukkit.getScheduler().scheduleSyncRepeatingTask(guiyPlugin, {
                if (hasFrameWaiters) {
                    hasFrameWaiters = false
                    clock.sendFrame(System.nanoTime()) // Frame time value is not used by Compose runtime.
                    rootNode.measure(Constraints())
                    rootNode.render()
                }
                delay(50)
            }
            running = false
            recomposer.close()
            snapshotHandle.dispose()
            composition.dispose()
            viewModels.values.forEach { it.close() }
            composeScope.cancel()
//            mainThreadScope.cancel()
        }
    }

    private fun setContent(content: @Composable () -> Unit) {
        hasFrameWaiters = true
        composition.setContent {
            CompositionLocalProvider(LocalClickHandler provides object : ClickHandler {
                override fun processClick(scope: ClickScope): ClickResult {
                    val slot = scope.slot
                    val width = rootNode.width
                    return rootNode.children.fold(ClickResult()) { acc, node ->
                        val w = node.width
                        val x = if (w == 0) 0 else slot % width
                        val y = if (w == 0) 0 else slot / width
                        acc.mergeWith(rootNode.processClick(scope, x, y))
                    }
                }

                override fun processDrag(scope: DragScope) {
                    rootNode.processDrag(scope)
                }
            }) {
                content()
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
            GuiyCompositionLocal(this) {
                content()
            }
        }
    }
}

@Composable
fun GuiyCompositionLocal(owner: GuiyOwner, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalGuiyOwner provides owner
    ) {
        content()
    }
}
