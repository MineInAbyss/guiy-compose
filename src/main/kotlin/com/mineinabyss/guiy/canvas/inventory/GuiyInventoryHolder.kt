package com.mineinabyss.guiy.canvas.inventory

import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.modifiers.drag.DragScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class GuiyInventoryHolder : InventoryHolder {
    private val _activeInventory = MutableStateFlow<GuiyInventory?>(null)
    val activeInventory = _activeInventory.asStateFlow()

    override fun getInventory(): Inventory =
        activeInventory.value?.inventory ?: error("Guiy inventory is used in bukkit but has not been rendered yet.")

    fun setActiveInventory(inventory: GuiyInventory) = _activeInventory.update { inventory }

    fun closeIfActive(inventory: Inventory) = _activeInventory.update {
        if(it?.inventory == inventory) null else it
    }

    abstract fun processClick(scope: ClickScope, event: Cancellable)
    abstract fun processDrag(scope: DragScope)

    abstract fun onClose(player: Player)

    abstract fun closeIfNoLongerViewing(player: Player)

    fun close() {
        inventory.viewers.forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
    }
}
