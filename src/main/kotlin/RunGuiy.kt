import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.GuiyScope
import com.mineinabyss.guiy.nodes.BoxNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import kotlinx.coroutines.*

fun runGuiy(body: suspend GuiyScope.() -> Unit) {
//    runBlocking(Dispatchers.Default) {
    awaitGuiy(body)
//    }
}

private object YieldFrameClock : MonotonicFrameClock {
    override suspend fun <R> withFrameNanos(
        onFrame: (frameTimeNanos: Long) -> R
    ): R {
        // We call `yield` to avoid blocking UI thread. If we don't call this then application
        // can be frozen for the user in some cases as it will not receive any input events.
        //
        // Swing dispatcher will process all pending events and resume after `yield`.
        yield()
        return onFrame(System.nanoTime())
    }
}

@OptIn(ExperimentalComposeApi::class)
fun awaitGuiy(body: suspend GuiyScope.() -> Unit) = runBlocking {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock {
        hasFrameWaiters = true
    }

    val job = Job(coroutineContext[Job])
    val composeContext = clock + coroutineContext + job

    val rootNode = BoxNode()
    val recomposer = Recomposer(composeContext)
    val composition = Composition(GuiyNodeApplier(rootNode), recomposer)

    // Start undispatched to ensure we can use suspending things inside the content.
    launch(context = composeContext) {
        recomposer.runRecomposeAndApplyChanges()
    }

    var displaySignal: CompletableDeferred<Unit>? = null
    launch(context = composeContext) {
        while (true) {
            if (hasFrameWaiters) {
                hasFrameWaiters = false
                clock.sendFrame(0L) // Frame time value is not used by Compose runtime.

//                output.display(rootNode.render())
                displaySignal?.complete(Unit)
            }
            delay(50)
        }
    }

    coroutineScope {
        val scope = object : GuiyScope, CoroutineScope by this {
            override fun exit() {
                displaySignal?.complete(Unit)
            }

            override fun setContent(content: @Composable () -> Unit) {
                composition.setContent(content)
                hasFrameWaiters = true
            }
        }

        var applyScheduled = false
        val snapshotHandle = Snapshot.registerGlobalWriteObserver {
            if (!applyScheduled) {
                applyScheduled = true
                scope.launch {
                    applyScheduled = false
                    Snapshot.sendApplyNotifications()
                }
            }
        }
        try {
            scope.body()
            recomposer.join()
        } finally {
            recomposer.close()
            snapshotHandle.dispose()
        }
    }
        // Ensure the final state modification is discovered. We need to ensure that the coroutine
        // which is running the recomposition loop wakes up, notices the changes, and waits for the
        // next frame. If you are using snapshots this only requires a single yield. If you are not
        // then it requires two yields. THIS IS NOT GREAT! But at least it's implementation detail...
        // TODO https://issuetracker.google.com/issues/169425431

        composition.dispose()
        recomposer.close()

        if (hasFrameWaiters) {
            CompletableDeferred<Unit>().also {
                displaySignal = it
                it.await()
            }
        }

        job.cancel()
        composition.dispose()
}
