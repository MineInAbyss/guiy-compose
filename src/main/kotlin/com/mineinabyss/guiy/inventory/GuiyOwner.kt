package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.modifiers.Constraints
import com.mineinabyss.guiy.modifiers.drag.DragScope
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

val LocalClickHandler: ProvidableCompositionLocal<ClickHandler> =
    staticCompositionLocalOf { error("No provider for local click handler") }
val LocalCanvas: ProvidableCompositionLocal<GuiyCanvas?> =
    staticCompositionLocalOf { null }
val LocalGuiyOwner: ProvidableCompositionLocal<GuiyOwner> =
    staticCompositionLocalOf { error("No provider for GuiyOwner") }

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
class GuiyOwner : CoroutineScope {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }
    val composeScope = CoroutineScope(Dispatchers.Default) + clock
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

    fun exit() {
        exitScheduled = true
    }

    fun start(content: @Composable () -> Unit) {
        !running || return
        running = true

        GuiyScopeManager.scopes += composeScope
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
            composeScope.cancel()
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
    content: @Composable () -> Unit
): GuiyOwner {
    return GuiyOwner().apply {
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
