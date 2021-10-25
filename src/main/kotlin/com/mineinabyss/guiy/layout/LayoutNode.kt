package com.mineinabyss.guiy.layout

import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.PositionModifier
import com.mineinabyss.guiy.nodes.ChildHoldingNode
import com.mineinabyss.guiy.nodes.GuiyNode

internal class LayoutNode : Measurable, Placeable, GuiyNode, ChildHoldingNode {
    override var measurer: Measurer = ErrorMeasurer
    override var placer: Placer = ErrorPlacer

    override val children = mutableListOf<LayoutNode>()
    override var modifier: Modifier = Modifier
    var parent: LayoutNode? = null
        private set

    override var width: Int = 0
    override var height: Int = 0
    override var x: Int = 0
    override var y: Int = 0

    override fun measure(): MeasureResult {
        val result = measurer.measure(children)
        width = result.width
        height = result.height
        return result
    }

    override fun placeChildren() {
        for (child in children) {
            val positionModifier = child.modifier.foldOut<PositionModifier?>(null) { element, accumulated ->
                if (element is PositionModifier) element else accumulated
            }
        }

        placer.placeChildren(children)
    }

    override fun placeAt(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    override fun renderTo(canvas: GuiyCanvas) {
        for (child in children) child.renderTo(canvas)
    }

    override fun toString() = children.joinToString(prefix = "Box(", postfix = ")")

    internal companion object {
        private val ErrorMeasurer = Measurer { error("Measurer not defined") }
        private val ErrorPlacer = Placer { error("Placer not defined") }
    }
}
