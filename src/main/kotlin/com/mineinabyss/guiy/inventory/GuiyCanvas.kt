package com.mineinabyss.guiy.inventory

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface GuiyCanvas {
    fun set(inventory: Inventory, x: Int, y: Int, item: ItemStack?)
}
