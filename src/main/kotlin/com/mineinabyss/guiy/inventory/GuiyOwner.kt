package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.modifiers.Constraints
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.nodes.InventoryCanvas
import com.okkero.skedule.schedule
import kotlinx.coroutines.*
import org.bukkit.event.inventory.InventoryCloseEvent
import kotlin.coroutines.CoroutineContext

@GuiyUIScopeMarker
class GuiyOwner : CoroutineScope {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }
    val composeScope = CoroutineScope(Dispatchers.Default) + clock
    override val coroutineContext: CoroutineContext = composeScope.coroutineContext

    private val rootNode = LayoutNode()
    internal var canvas: InventoryCanvas? = null

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

    fun start(content: @Composable GuiyOwner.() -> Unit) {
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
                    canvas?.render()
                }
                delay(50)
            }
            running = false
            guiyPlugin.schedule {
                canvas?.viewers?.forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
            }
            recomposer.close()
            snapshotHandle.dispose()
            composition.dispose()
            composeScope.cancel()
        }
    }

    private fun setContent(content: @Composable GuiyOwner.() -> Unit) {
        hasFrameWaiters = true
        composition.setContent {
            content()
        }
    }
}

fun guiy(
    content: @Composable GuiyOwner.() -> Unit
): GuiyOwner {
    return GuiyOwner().apply {
        start(content)
    }
}
