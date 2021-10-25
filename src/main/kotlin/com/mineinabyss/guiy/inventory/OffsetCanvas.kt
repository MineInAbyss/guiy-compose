package com.mineinabyss.guiy.inventory

import org.bukkit.inventory.ItemStack

class OffsetCanvas(
    val offsetX: Int,
    val offsetY: Int,
    val delegate: GuiyCanvas,
): GuiyCanvas {
    override fun set(x: Int, y: Int, item: ItemStack?) {
        delegate.set(x + offsetX, y + offsetX, item)
    }

    override fun clear() {
        delegate.clear()
    }
}
