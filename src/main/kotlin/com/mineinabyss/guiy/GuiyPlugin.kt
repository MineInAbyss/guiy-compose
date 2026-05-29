package com.mineinabyss.guiy

import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.dependencies.DI
import com.mineinabyss.dependencies.DIContext
import com.mineinabyss.dependencies.getLazy
import com.mineinabyss.guiy.canvas.GuiyScopeManager
import com.mineinabyss.guiy.canvas.inventory.GuiyEventListener
import com.mineinabyss.guiy.canvas.inventory.GuiyInventoryHolder
import com.mineinabyss.idofront.features.singlePluginLogger
import com.mineinabyss.idofront.messaging.ComponentLogger
import com.mineinabyss.idofront.nms.interceptServerbound
import com.mineinabyss.idofront.plugin.listeners
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

val guiyPlugin: GuiyPlugin = Bukkit.getPluginManager().getPlugin("Guiy") as GuiyPlugin

@OptIn(ExperimentalAtomicApi::class)
class GuiyPlugin : JavaPlugin(), DI {
    override val di: DIContext = DI {
        singlePluginLogger(this@GuiyPlugin)
    }
    val logger by di.getLazy<ComponentLogger>()

    val anvilPacketFlow = MutableStateFlow<Pair<String, Player>?>(null)
    private val applyScheduled = AtomicBoolean(false)
    val frameClock = BroadcastFrameClock()
    private val snapshotHandle = Snapshot.registerGlobalWriteObserver {
        applyScheduled.compareAndSet(expectedValue = false, newValue = true)
    }

    override fun onEnable() {
        listeners(GuiyEventListener())
        interceptServerbound { packet, player: Player? ->
            if (packet is ServerboundRenameItemPacket && player != null) {
                anvilPacketFlow.update { packet.name to player }
            }
            packet
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            // Apply compose snapshot updates
            if (applyScheduled.compareAndSet(expectedValue = true, newValue = false)) {
                Snapshot.sendApplyNotifications()
            }

            // Dispatch frame update for all UIs running on the server
            frameClock.sendFrame(System.nanoTime())

        }, 0L, 1L)
    }

    override fun onDisable() {
        GuiyScopeManager.scopes.forEach { it.cancel() }
        Bukkit.getOnlinePlayers()
            .mapNotNull { it.openInventory.topInventory.holder as? GuiyInventoryHolder }
            .forEach { it.close() }
        snapshotHandle.dispose()
    }

    companion object {
        var instance: GuiyPlugin? = null
    }
}
