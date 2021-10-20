package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas

internal open class BoxNode : GuiyNode(), ChildHoldingNode {
    override val children = mutableListOf<GuiyNode>()

    /** If row, otherwise column. */
    var isRow = true

    override fun measure() {
        if (isRow) {
            measureRow()
        } else {
            measureColumn()
        }
    }

    private fun measureRow() {
        var width = 0
        var height = 0
        for (child in children) {
            child.measure()
            width += child.width
            height = maxOf(height, child.height)
        }
        this.width = width
        this.height = height
    }

    private fun measureColumn() {
        var width = 0
        var height = 0
        for (child in children) {
            child.measure()
            width = maxOf(width, child.width)
            height += child.height
        }
        this.width = width
        this.height = height
    }

    override fun layout() {
        if (isRow) {
            layoutRow()
        } else {
            layoutColumn()
        }
    }

    private fun layoutRow() {
        var childX = 0
        for (child in children) {
            child.x = childX
            child.y = 0
            child.layout()
            childX += child.width
        }
    }

    private fun layoutColumn() {
        var childY = 0
        for (child in children) {
            child.x = 0
            child.y = childY
            child.layout()
            childY += child.height
        }
    }

    override fun renderTo(canvas: GuiyCanvas) {
        for (child in children) {
            val left = child.x
            val top = child.y
            val right = left + child.width - 1
            val bottom = top + child.height - 1
            child.renderTo(canvas[top..bottom, left..right])
        }
    }

    override fun processClick(x: Int, y: Int) {
        super.processClick(x, y)
        children.firstOrNull { x in it.x until (it.x + it.width) && y in it.y until (it.y + it.height) }
            ?.processClick(x, y)
    }

    override fun toString() = children.joinToString(prefix = "Box(", postfix = ")")
}
