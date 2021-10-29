package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.components.GuiyUIScopeMarker
import com.mineinabyss.guiy.layout.ChildPlacer
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.nodes.InventoryCanvas
import kotlinx.coroutines.*
import org.bukkit.entity.Player
import kotlin.coroutines.CoroutineContext

@GuiyUIScopeMarker
class GuiyOwner : CoroutineScope {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }
    val composeScope = CoroutineScope(Dispatchers.Default) + clock
    override val coroutineContext: CoroutineContext = composeScope.coroutineContext

    private val rootNode = LayoutNode()
    val viewers by derivedStateOf { mutableStateListOf<Player>() }
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

    fun exit() {
        running = false
        recomposer.close()
        snapshotHandle.dispose()
        composition.dispose()
        GuiyScopeManager.scopes -= composeScope
        viewers.forEach { it.closeInventory() }
        composeScope.cancel()
    }

    fun start(content: @Composable GuiyOwner.() -> Unit) {
        !running || return
        running = true

        GuiyScopeManager.scopes += composeScope
        launch {
            recomposer.runRecomposeAndApplyChanges()
        }
        launch {
            while (true) {
                if (hasFrameWaiters) {
                    hasFrameWaiters = false
                    clock.sendFrame(0L) // Frame time value is not used by Compose runtime.

                    rootNode.measure()
                    rootNode.placeChildren()
                    canvas?.render()
                }
                delay(50)
            }
        }

        launch {
            setContent(content)
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