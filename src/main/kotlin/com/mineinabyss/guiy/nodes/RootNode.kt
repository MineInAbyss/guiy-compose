package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas

internal class RootNode : GuiyNode(), ChildHoldingNode {
    override val children: MutableList<GuiyNode> = mutableListOf()
    var activeInventory: InventoryCanvas? = null

    fun renderToFirstCanvas() {
        val child = children.filterIsInstance<InventoryCanvas>().firstOrNull() ?: return
        activeInventory = child
        child.measure()
        child.layout()
        child.render()
    }

    override fun measure() {}

    override fun layout() {}

    override fun renderTo(canvas: GuiyCanvas) {}
}
