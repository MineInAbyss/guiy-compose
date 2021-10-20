package com.mineinabyss.guiy.inventory

import org.bukkit.inventory.ItemStack

internal interface GuiyCanvas {
    val width: Int
    val height: Int

    operator fun get(rows: IntRange, columns: IntRange): GuiyCanvas {
        val top = rows.first
        require(top in 0 until height) { "Row start value out of range [0,$height): $top" }
        val bottom = rows.last
        require(bottom < height) { "Row end value out of range [0,$height): $bottom" }
        val left = columns.first
        require(left in 0 until width) { "Column start value out of range [0,$width): $left" }
        val right = columns.last
        require(right < width) { "Column end value out of range [0,$width): $right" }

        return ClippedGuiyCanvas(this, left, top, right, bottom)
    }

    fun set(x: Int, y: Int, item: ItemStack?)

    fun clear(left: Int = 0, top: Int = 0, right: Int = width - 1, bottom: Int = height - 1)
}

//TODO does mc not allow any other widths?
const val INVENTORY_WIDTH = 9
