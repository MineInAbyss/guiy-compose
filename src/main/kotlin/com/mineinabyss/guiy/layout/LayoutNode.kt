package com.mineinabyss.guiy.layout

import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.inventory.OffsetCanvas
import com.mineinabyss.guiy.modifiers.ClickScope
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.PositionModifier
import com.mineinabyss.guiy.modifiers.getClickModifiers
import com.mineinabyss.guiy.nodes.GuiyNode
import org.bukkit.event.inventory.ClickType

/**
 * TODO structure is really not decided on yet.
 *  I'd really like to avoid inheritance, and have only one ComposableNode call that creates Layout.
 *  You can configure stuff through [measurer], [placer], and the [modifier], but things creates some problems
 *  when trying to make your own composable nodes that interact with this Layout node.
 */
internal open class LayoutNode : Measurable, Placeable, GuiyNode {
    final override var measurer: Measurer = Measurer {
        children.forEach { it.measure() }
        MeasureResult(width, height)
    }
    final override var placer: Placer = ChildPlacer
    final override var renderer: Renderer = EmptyRenderer

    val children = mutableListOf<LayoutNode>()
    override var modifier: Modifier = Modifier
    var parent: LayoutNode? = null

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
        val absolutePositioned = children.filter { child ->
            val positionModifier = child.modifier.foldOut<PositionModifier?>(null) { element, accumulated ->
                if (element is PositionModifier) element else accumulated
            } ?: return@filter false
            child.placeAt(positionModifier.x, positionModifier.y)
            child.placeChildren()
            true
        }

        placer.placeChildren(children - absolutePositioned)
    }

    override fun placeAt(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    override fun renderTo(canvas: GuiyCanvas) {
        val offsetCanvas = OffsetCanvas(x, y, canvas)
        //TODO AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        // Yeah Google likes to do this sorta stuff through Modifiers but their architecture might
        // be a bit too much for the tiny UIs we'd use in Minecraft, this is a temporary compromise.
        renderer.apply { offsetCanvas.render(this@LayoutNode) }
        for (child in children) child.renderTo(offsetCanvas)
    }

    override fun toString() = children.joinToString(prefix = "Box(", postfix = ")")

    fun processClick(x: Int, y: Int, type: ClickType) {
        val scope = object : ClickScope {
            override val clickType: ClickType = type
        }

        modifier.getClickModifiers().forEach { it.onClick(scope) }
        children.filter { x in it.x until (it.x + it.width) && y in it.y until (it.y + it.height) }
            .forEach { it.processClick(x - it.x, y - it.y, type) }
    }

    internal companion object {
        val EmptyRenderer = Renderer {  }
        private val ErrorMeasurer = Measurer { error("Measurer not defined") }
        private val ErrorPlacer = Placer { error("Placer not defined") }
    }
}
