package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.layout.Measurer
import com.mineinabyss.guiy.layout.Placer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

internal class ItemNode: LayoutNode() {
    var item: ItemStack = ItemStack(Material.AIR)

    init {
        measurer = Measurer { MeasureResult(1, 1) }
        placer = Placer {  }
    }

    override fun renderTo(canvas: GuiyCanvas) {
        canvas.set(x, y, item)
    }
}
