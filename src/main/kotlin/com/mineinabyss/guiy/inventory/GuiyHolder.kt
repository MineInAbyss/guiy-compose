package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.nodes.RootNode
import kotlinx.coroutines.*
import net.kyori.adventure.text.Component
import kotlin.coroutines.CoroutineContext

class GuiyHolder : CoroutineScope {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }
    val composeScope = CoroutineScope(Dispatchers.Default) + clock
    override val coroutineContext: CoroutineContext = composeScope.coroutineContext

    private val rootNode = RootNode()

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
        composeScope.cancel()
    }

    fun start(content: @Composable GuiyHolder.() -> Unit) {
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

                    rootNode.renderToFirstCanvas()
                }
                delay(50)
            }
        }

        launch {
            setContent(content)
            recomposer.join()
        }
    }

    private fun setContent(content: @Composable GuiyHolder.() -> Unit) {
        hasFrameWaiters = true
        composition.setContent {
            content()
        }
    }
}

fun guiy(
    content: @Composable GuiyHolder.() -> Unit
): GuiyHolder {
    return GuiyHolder().apply {
        start(content)
    }
}
