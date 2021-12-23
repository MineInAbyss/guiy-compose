package com.mineinabyss.guiy

import com.mineinabyss.guiy.inventory.GuiyEventListener
import com.mineinabyss.guiy.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.inventory.GuiyScopeManager
import com.mineinabyss.idofront.platforms.IdofrontPlatforms
import com.mineinabyss.idofront.plugin.registerEvents
import kotlinx.coroutines.cancel
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

val guiyPlugin = Bukkit.getPluginManager().getPlugin("Guiy") as JavaPlugin

class GuiyPlugin : JavaPlugin() {
    override fun onLoad() {
        IdofrontPlatforms.load(this, "mineinabyss")
    }

    override fun onEnable() {
        registerEvents(GuiyEventListener())
    }

    override fun onDisable() {
        GuiyScopeManager.scopes.forEach { it.cancel() }
        Bukkit.getOnlinePlayers()
            .mapNotNull { it.openInventory.topInventory.holder as? GuiyInventoryHolder }
            .forEach { it.close() }
    }
}
