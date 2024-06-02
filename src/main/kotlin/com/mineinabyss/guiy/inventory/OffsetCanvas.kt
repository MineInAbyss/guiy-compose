package com.mineinabyss.guiy.inventory

import org.bukkit.inventory.ItemStack

data class OffsetCanvas(
    val offsetX: Int,
    val offsetY: Int,
    val delegate: GuiyCanvas,
): GuiyCanvas by delegate {
    override fun set(x: Int, y: Int, item: ItemStack?) {
        delegate.set(x + offsetX, y + offsetY, item)
    }
}
