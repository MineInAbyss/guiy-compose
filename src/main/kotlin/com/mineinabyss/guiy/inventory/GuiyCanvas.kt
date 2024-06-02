package com.mineinabyss.guiy.inventory

import com.mineinabyss.guiy.components.state.IntCoordinates
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.bukkit.inventory.ItemStack

interface GuiyCanvas {
    fun set(x: Int, y: Int, item: ItemStack?)
}

open class MapBackedGuiyCanvas : GuiyCanvas {
    private val contents = Long2ObjectOpenHashMap<ItemStack>()

    override fun set(x: Int, y: Int, item: ItemStack?) {
        if (item == null) contents.remove(IntCoordinates(x, y).pair)
        else contents[IntCoordinates(x, y).pair] = item
    }

    fun startRender() {
        contents.clear()
    }

    fun getCoordinates(): Map<IntCoordinates, ItemStack> {
        return contents.toMap().mapKeys { IntCoordinates(it.key) }
    }
}
