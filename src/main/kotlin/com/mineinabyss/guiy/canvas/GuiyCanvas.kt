package com.mineinabyss.guiy.canvas

import com.mineinabyss.guiy.components.state.IntCoordinates
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.bukkit.inventory.ItemStack

interface GuiyCanvas {
    fun subCanvas(x: Int, y: Int): GuiyCanvas
}

class InventoryCanvas private constructor(
    private val contents: Long2ObjectOpenHashMap<ItemStack>,
    private val offX: Int,
    private val offY: Int,
) : GuiyCanvas {
    constructor(): this(Long2ObjectOpenHashMap(), 0, 0)

    fun set(x: Int, y: Int, item: ItemStack?) {
        if (item == null) contents.remove(IntCoordinates(x + offX, y + offY).pair)
        else contents[IntCoordinates(x + offX, y + offY).pair] = item
    }

    fun clear() {
        contents.clear()
    }

    fun contents(): Map<IntCoordinates, ItemStack> {
        return contents.toMap().mapKeys { IntCoordinates(it.key) }
    }

    override fun subCanvas(x: Int, y: Int): GuiyCanvas =
        InventoryCanvas(contents, offX + x, offY + y)
}