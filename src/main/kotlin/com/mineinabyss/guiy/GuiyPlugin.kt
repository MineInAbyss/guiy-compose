package com.mineinabyss.guiy

import com.mineinabyss.guiy.inventory.GuiyEventListener
import com.mineinabyss.guiy.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.inventory.GuiyScopeManager
import com.mineinabyss.idofront.nms.interceptServerbound
import com.mineinabyss.idofront.plugin.listeners
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import net.minecraft.network.Connection
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

val guiyPlugin: GuiyPlugin = Bukkit.getPluginManager().getPlugin("Guiy") as GuiyPlugin

class GuiyPlugin : JavaPlugin() {
    val anvilPacketFlow = MutableStateFlow<Pair<String, Player>?>(null)

    override fun onEnable() {
        listeners(GuiyEventListener())
        interceptServerbound { packet, player: Player? ->
            if(packet is ServerboundRenameItemPacket && player != null) {
                anvilPacketFlow.update { packet.name to player }
            }
            packet
        }
    }

    override fun onDisable() {
        GuiyScopeManager.scopes.forEach { it.cancel() }
        Bukkit.getOnlinePlayers()
            .mapNotNull { it.openInventory.topInventory.holder as? GuiyInventoryHolder }
            .forEach { it.close() }
    }
}
