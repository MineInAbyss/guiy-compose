package com.mineinabyss.guiy.draw

import androidx.compose.ui.unit.IntOffset
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.bukkit.inventory.ItemStack

class InventoryCanvas private constructor(
    private val contents: Long2ObjectOpenHashMap<ItemStack>,
    private val offX: Int,
    private val offY: Int,
)  {
    constructor() : this(Long2ObjectOpenHashMap(), 0, 0)

    fun set(x: Int, y: Int, item: ItemStack?) {
        if (item == null) contents.remove(IntOffset(x + offX, y + offY).packedValue)
        else contents[IntOffset(x + offX, y + offY).packedValue] = item
    }

    fun clear() {
        contents.clear()
    }

    fun contents(): Map<IntOffset, ItemStack> {
        return contents.toMap().mapKeys { IntOffset(it.key) }
    }
}