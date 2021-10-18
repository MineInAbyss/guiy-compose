package com.mineinabyss.guiy.inventory

import org.bukkit.inventory.ItemStack

internal class ClippedGuiyCanvas(
    private val parent: GuiyCanvas,
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int,
) : GuiyCanvas {
    override val width = right - left + 1
    override val height = bottom - top + 1

    override fun set(x: Int, y: Int, item: ItemStack?) {
        parent.set(x + left, y + top, item)
    }

    override fun clear(left: Int, top: Int, right: Int, bottom: Int) {
        parent.clear(this.left + left, this.top + top, this.left + right, this.bottom + bottom)
    }
}

