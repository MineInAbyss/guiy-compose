package com.mineinabyss.guiy.canvas.inventory

import com.mineinabyss.guiy.interaction.ClickEvent
import com.mineinabyss.guiy.modifiers.scroll.BundleScrolling
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
        val guiyHolder = inventory.getHolder(false) as? GuiyInventoryHolder ?: return
        // A cancelled click resends the slots, which resets the client's bundle selection
        BundleScrolling.reset(whoClicked as Player)

        // Avoid any exploits shift clicking or double-clicking into/from the GUI
        if (click !in setOf(LEFT, RIGHT, MIDDLE)) isCancelled = true

        val clickedInventory = clickedInventory ?: return
        if (clickedInventory.getHolder(false) !== guiyHolder) return
        isCancelled = true

        val event = ClickEvent.fromSlot(click, slot, whoClicked)
        guiyHolder.onClick(event, this)
    }

    @EventHandler
    fun InventoryDragEvent.onInventoryDrag() {
        val guiyHolder = inventory.getHolder(false) as? GuiyInventoryHolder ?: return
//        val inPlayerInv = newItems.filter { it.key >= view.topInventory.size }
        val inGuiy = newItems.filter { it.key < view.topInventory.size }
        // Handle single slot drag events as clicks for better responsiveness
        if (newItems.size == 1 && inGuiy.size == 1) {
            isCancelled = true
            val clicked = inGuiy.entries.first()
            val scope = ClickEvent.fromSlot(LEFT, clicked.key, whoClicked)
            guiyHolder.onClick(scope, this)
        } else if (inGuiy.isNotEmpty()) {
            isCancelled = true
        }
    }

    @EventHandler
    fun InventoryCloseEvent.onClose() {
        val guiyHolder = inventory.getHolder(false) as? GuiyInventoryHolder ?: return
        BundleScrolling.reset(player as Player)

        if (reason != InventoryCloseEvent.Reason.PLUGIN) {
            guiyHolder.onClose(player as Player)
        } else {
            guiyHolder.closeIfNoLongerViewing(player as Player)
        }
    }
}
