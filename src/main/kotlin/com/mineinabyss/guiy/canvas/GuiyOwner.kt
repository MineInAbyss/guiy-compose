package com.mineinabyss.guiy.canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Updater
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.unit.Constraints
import com.mineinabyss.guiy.components.canvases.InventoryHolder
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.interaction.ClickEvent
import com.mineinabyss.guiy.layout.GuiyNode
import com.mineinabyss.guiy.layout.GuiyNodeDrawScope
import com.mineinabyss.guiy.navigation.BackGestureDispatcher
import com.mineinabyss.guiy.navigation.LocalBackGestureDispatcher
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.viewmodel.GuiyViewModel
import com.mineinabyss.idofront.messaging.injectedLogger
import me.dvyy.compose.mini.layout.ComposeMiniNode
import me.dvyy.compose.mini.layout.LayoutFactory
import me.dvyy.compose.mini.layout.LocalLayoutFactory
import me.dvyy.compose.mini.runtime.MinimalComposition
import org.bukkit.entity.Player
import kotlin.reflect.KType

object GuiyLayoutFactory : LayoutFactory {
    override val create: () -> ComposeMiniNode = GuiyNode.Constructor
    override val update: Updater<ComposeMiniNode>.() -> Unit = {}

}

@GuiyUIScopeMarker
class GuiyOwner(
    initialViewers: Set<Player> = setOf(),
) {
    private val rootNode = GuiyNode()
    val logger = guiyPlugin.injectedLogger()

    //FIXME I think SnapshotHandle impl here will set applyScheduled to true for ALL GuiyOwner instances when any of them update?
    private val composition = MinimalComposition(
        // We use our own GuiyUIDispatcher which acts like an Dispatchers.Main.immediate
        // Immediate is important for compose to correctly finish recompositions in one cycle (it does operations that will yield several times otherwise.)
        //TODO come up with a test that breaks when not using an immediate style dispatcher.
        coroutineContext = GuiyUIDispatcher.Main,
        onNewFrame = { update() },
        wrapContent = { content ->
            CompositionLocalProvider(
                LocalGuiyOwner provides this,
                LocalLayoutFactory provides GuiyLayoutFactory,
                LocalBackGestureDispatcher provides BackGestureDispatcher(),
            ) {
                // A default inventory holder for most usecases
                InventoryHolder(
                    initialViewers,
                    onViewersChange = {
                        logger.v { "Viewers changed to $it" }
                        if (it.isEmpty()) exit()
                    }
                ) {
                    content()
                }
            }
        },
        createLayerNode = { GuiyNode().also { rootNode.children.add(it) } },
        removeLayerNode = { rootNode.children.remove(it) },
        applierForNode = { GuiyNodeApplier(it, onChanges = { requiresLayout = true }) }
    )

    //    val mainThreadScope = CoroutineScope(guiyPlugin.minecraftDispatcher) + SupervisorJob()
    private val viewModels = mutableMapOf<KType, GuiyViewModel>()

    var requiresLayout = true
    var requiresDraw = true

    private val readStatesOnLayout = mutableSetOf<Any>()//TODO mutableScatterSetOf<Any>()
    private val readStatesOnLayoutObserver: (Any) -> Unit = readStatesOnLayout::add
    private val readStatesOnDraw = mutableSetOf<Any>()//TODO mutableScatterSetOf<Any>()
    private val readStatesOnDrawObserver: (Any) -> Unit = readStatesOnDraw::add


    fun update() {
        if (requiresLayout) {
            requiresLayout = false
            Snapshot.observe(readObserver = readStatesOnLayoutObserver) {
                rootNode.layoutDelegate.measure(constraints = Constraints())
                rootNode.layoutDelegate.placeAt(0, 0)
            }

            requiresDraw = true
        }
        if (requiresDraw) {
            requiresDraw = false
            Snapshot.observe(readObserver = readStatesOnDrawObserver) {
                rootNode.layoutDelegate.drawTo(GuiyNodeDrawScope())
            }
        }
    }

    fun handleClick(event: ClickEvent) {
        rootNode.layoutDelegate.processClickEvent(event)
    }

    fun exit() {
        composition.close()
    }

    fun start(
        content: @Composable () -> Unit,
    ) {
        composition.start { content() }
    }

    fun <T> getViewModel(type: KType): T? {
        return viewModels[type] as? T
    }

    fun addViewModel(type: KType, viewModel: GuiyViewModel) {
        viewModels[type] = viewModel
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
