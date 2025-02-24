package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.modifiers.drag.DragScope
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class GuiyInventoryHolder : InventoryHolder {
    var activeInventory: Inventory? by mutableStateOf(null)

    override fun getInventory(): Inventory =
        activeInventory ?: error("Guiy inventory is used in bukkit but has not been rendered yet.")

    abstract fun processClick(scope: ClickScope, event: Cancellable)
    abstract fun processDrag(scope: DragScope)

    abstract fun onClose(player: Player)

    abstract fun forceClose(player: Player)

    fun close() {
        inventory.viewers.forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
    }
}
