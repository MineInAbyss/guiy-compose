package com.mineinabyss.guiy.inventory

import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.modifiers.DragScope
import com.mineinabyss.idofront.nms.aliases.NMSItemStack
import com.mineinabyss.idofront.nms.aliases.NMSPlayer
import com.mineinabyss.idofront.nms.aliases.toNMS
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.event.inventory.ClickType.*
import org.bukkit.inventory.ItemStack
import kotlin.math.abs

class GuiyEventListener : Listener {
    @EventHandler
    fun InventoryClickEvent.onClick() {
        val guiyHolder = inventory.holder as? GuiyInventoryHolder ?: return
        if (click !in setOf(LEFT, RIGHT, MIDDLE)) isCancelled = true
        if (clickedInventory?.holder === guiyHolder)
            guiyHolder.processClick(this)
    }

    @EventHandler
    fun InventoryOpenEvent.onOpen() {
        val guiyHolder = inventory.holder as? GuiyInventoryHolder ?: return
        guiyHolder.onOpen(player as Player)
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
        val inPlayerInv = newItems.filter { it.key >= view.topInventory.size }
        val inGuiy = newItems.filter { it.key < view.topInventory.size }
        if (inGuiy.isNotEmpty()) {
            isCancelled = true
            result = Event.Result.DEFAULT
            val newCursor = oldCursor.apply { inGuiy.map { inventory.getItem(it.key)?.amount ?: 0 }.sum() }
            val event = InventoryDragEvent(
                view, newCursor, oldCursor, type == DragType.SINGLE, inPlayerInv
            )
            event.callEvent()
            if (!event.isCancelled) {
                for ((slot, item) in inPlayerInv) {
                    newCursor.amount -= abs((view.getItem(slot)?.amount ?: 0) - item.amount)
                    view.setItem(slot, item)
                }

                val scope = DragScope(type, inGuiy, newCursor)
                guiyHolder.processDrag(scope)
                guiyPlugin.launch {
                    delay(1)
                    val nmsView = whoClicked.toNMS<NMSPlayer>().containerMenu
                    nmsView.carried = NMSItemStack.fromBukkitCopy(scope.cursor ?: ItemStack(Material.AIR))
                    nmsView.sendAllDataToRemote()
                }
            }
        }

    }
}
