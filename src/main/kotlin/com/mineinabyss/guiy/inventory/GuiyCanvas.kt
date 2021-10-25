package com.mineinabyss.guiy.inventory

import org.bukkit.inventory.ItemStack

interface GuiyCanvas {
    fun set(x: Int, y: Int, item: ItemStack?)

    fun clear()
}

const val MAX_CHEST_WIDTH = 9
const val MAX_CHEST_HEIGHT = 6
