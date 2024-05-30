package com.mineinabyss.guiy.inventory

import com.mineinabyss.guiy.modifiers.click.ClickScope
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class GuiyEventListener : Listener {
    @EventHandler
    fun InventoryClickEvent.onClick() {
        val guiyHolder = inventory.holder as? GuiyInventoryHolder ?: return

        // Avoid any exploits shift clicking or double-clicking into/from the GUI
        if (click !in setOf(LEFT, RIGHT, MIDDLE)) isCancelled = true

        val clickedInventory = clickedInventory ?: return
        if (clickedInventory.holder !== guiyHolder) return
        isCancelled = true

        val scope = ClickScope(
            click, slot, cursor.takeIf { it.type != Material.AIR }, whoClicked
        )
        guiyHolder.processClick(scope, this)
    }

    @EventHandler
    fun InventoryCloseEvent.onClose() {
        val guiyHolder = inventory.holder as? GuiyInventoryHolder ?: return

        if (reason != InventoryCloseEvent.Reason.PLUGIN) {
            guiyHolder.onClose(player as Player)
        }
    }


    @EventHandler
    fun InventoryDragEvent.onInventoryDrag() {
        val guiyHolder = inventory.holder as? GuiyInventoryHolder ?: return
//        val inPlayerInv = newItems.filter { it.key >= view.topInventory.size }
        val inGuiy = newItems.filter { it.key < view.topInventory.size }
        // Handle single slot drag events as clicks for better responsiveness
        if (newItems.size == 1 && inGuiy.size == 1) {
            isCancelled = true
            val clicked = inGuiy.entries.first()
            val scope = ClickScope(LEFT, clicked.key, cursor?.takeIf { it.type != Material.AIR }, whoClicked)
            guiyHolder.processClick(scope, this)
        } else if (inGuiy.isNotEmpty()) {
            isCancelled = true
        }
    }
}
