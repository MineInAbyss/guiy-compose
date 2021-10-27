package com.mineinabyss.guiy

import com.mineinabyss.guiy.inventory.GuiyEventListener
import com.mineinabyss.guiy.inventory.GuiyScopeManager
import com.mineinabyss.guiy.nodes.InventoryCanvas
import com.mineinabyss.idofront.plugin.registerEvents
import kotlinx.coroutines.cancel
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin

object Guiy {
    lateinit var activePlugin: JavaPlugin

    fun load(plugin: JavaPlugin) {
        activePlugin = plugin
        plugin.registerEvents(GuiyEventListener())
    }

    fun dispose() {
        GuiyScopeManager.scopes.forEach { it.cancel() }
        Bukkit.getOnlinePlayers().filter { it.openInventory.topInventory.holder is InventoryCanvas }
            .forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
    }
}
