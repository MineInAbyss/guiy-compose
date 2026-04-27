package com.mineinabyss.guiy.draw

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.bukkit.inventory.ItemStack

interface DrawScope {
    val width: Int
    val height: Int

    fun drawItem(x: Int, y: Int, itemStack: ItemStack?)
    fun drawItemRect(offset: IntOffset = IntOffset(0, 0), size: IntSize = IntSize(width, height), itemStack: ItemStack?)
}

interface ContentDrawScope: DrawScope {
    /** Causes child drawing operations to run. */
    fun drawContent()
}