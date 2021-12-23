package com.mineinabyss.guiy.inventory

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class OffsetCanvas(
    val offsetX: Int,
    val offsetY: Int,
    val delegate: GuiyCanvas,
): GuiyCanvas by delegate {
    override fun set(inventory: Inventory, x: Int, y: Int, item: ItemStack?) {
        delegate.set(inventory, x + offsetX, y + offsetY, item)
    }
}
