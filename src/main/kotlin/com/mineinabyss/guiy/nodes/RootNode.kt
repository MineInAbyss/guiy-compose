package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas

internal class RootNode : GuiyNode(), ChildHoldingNode {
    override val children: MutableList<GuiyNode> = mutableListOf()

    fun renderToFirstCanvas() {
        val child = children.filterIsInstance<InventoryNode>().firstOrNull() ?: return
        child.measure()
        child.layout()
        child.render()
    }

    override fun measure() {}

    override fun layout() {}

    override fun renderTo(canvas: GuiyCanvas) {}
}
