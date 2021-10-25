package com.mineinabyss.guiy.inventory

import com.mineinabyss.guiy.nodes.InventoryCanvas
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class GuiyEventListener : Listener {
    @EventHandler
    fun InventoryClickEvent.onClick() {
        val guiyHolder = inventory.holder as? InventoryCanvas ?: return
        isCancelled = true
        if (clickedInventory?.holder === guiyHolder)
            guiyHolder.processClick(click, slot)
    }

    @EventHandler
    fun InventoryCloseEvent.onClose() {
        val guiyHolder = inventory.holder as? InventoryCanvas ?: return
        if (reason != InventoryCloseEvent.Reason.PLUGIN) {
            guiyHolder.onClose(player as Player)
        }
    }


    @EventHandler
    fun InventoryDragEvent.onInventoryDrag() {
        if (inventory.holder !is InventoryCanvas) return
        isCancelled = true
    }
}
