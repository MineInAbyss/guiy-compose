package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

internal class ItemNode: GuiyNode() {
    var item: ItemStack = ItemStack(Material.AIR)

    init {
        width = 1
        height = 1
    }

    override fun measure() {}

    override fun layout() {}

    override fun renderTo(canvas: GuiyCanvas) {
        canvas.set(x, y, item)
    }
}
