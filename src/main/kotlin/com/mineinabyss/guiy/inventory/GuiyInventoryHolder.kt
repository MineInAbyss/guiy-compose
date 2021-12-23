package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class GuiyInventoryHolder : InventoryHolder {
    var activeInventory: Inventory? by mutableStateOf(null)

    override fun getInventory(): Inventory =
        activeInventory ?: error("Guiy inventory is used in bukkit but has not been rendered yet.")

    abstract fun processClick(slot: Int, clickType: ClickType)

    abstract fun onClose(player: Player)

    fun close() {
        inventory.viewers.forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
    }
}
