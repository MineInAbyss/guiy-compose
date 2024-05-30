package com.mineinabyss.guiy.components.state

import androidx.compose.runtime.Immutable
import org.bukkit.inventory.ItemStack

@Immutable
class ItemPositions(
    val items: Map<IntCoordinates, ItemStack> = emptyMap()
) {
    fun plus(x: Int, y: Int, item: ItemStack) =
        ItemPositions(items + (IntCoordinates(x, y) to item))

    fun minus(x: Int, y: Int) = ItemPositions(items - IntCoordinates(x, y))

    fun with(x: Int, y: Int, item: ItemStack?) = if (item == null) minus(x, y) else plus(x, y, item)

    operator fun get(x: Int, y: Int) = items[IntCoordinates(x, y)]
}
