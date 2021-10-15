package com.mineinabyss.guiy

import androidx.compose.runtime.snapshots.ObserverHandle
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

internal object GuiySnapshotManager {
    private val started = AtomicBoolean(false)
    private var commitPending = false
    private var removeWriteObserver: (ObserverHandle)? = null

    fun ensureStarted() {
        if (started.compareAndSet(false, true)) {
            val channel = Channel<Unit>(Channel.CONFLATED)
            CoroutineScope(Dispatchers.Default).launch {
                channel.consumeEach {
                    Snapshot.sendApplyNotifications()
                }
            }
            Snapshot.registerGlobalWriteObserver {
                CoroutineScope(Dispatchers.Default).launch {
                    Snapshot.sendApplyNotifications()
                }
                channel.trySend(Unit)
            }
        }
    }
}
