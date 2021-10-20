package com.mineinabyss.guiy.inventory

import com.mineinabyss.guiy.nodes.InventoryNode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class GuiyEventListener : Listener {
    @EventHandler
    fun InventoryClickEvent.onClick() {
        val guiyHolder = inventory.holder as? InventoryNode ?: return
        isCancelled = true
        if (clickedInventory?.holder === guiyHolder)
            guiyHolder.processClick(slot)
    }

    @EventHandler
    fun InventoryCloseEvent.onClose() {
        val guiyHolder = inventory.holder as? InventoryNode ?: return
        if (!guiyHolder.updateRunning)
            guiyHolder.onClose(player as Player)
    }


    @EventHandler
    fun InventoryDragEvent.onInventoryDrag() {
        if (inventory.holder !is InventoryNode) return
        isCancelled = true
    }
}
