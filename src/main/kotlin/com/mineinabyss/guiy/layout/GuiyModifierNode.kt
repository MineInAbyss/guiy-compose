package com.mineinabyss.guiy.layout

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.mineinabyss.guiy.draw.ContentDrawScope
import com.mineinabyss.guiy.draw.InventoryCanvas
import com.mineinabyss.guiy.interaction.ClickEvent
import com.mineinabyss.guiy.modifiers.DrawModifierNode
import com.mineinabyss.guiy.modifiers.InteractionModifierNode
import me.dvyy.compose.mini.layout.jetpack.*
import me.dvyy.compose.mini.layout.modifiers.LayoutAwareModifierNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierWrapper
import org.bukkit.inventory.ItemStack

internal class GuiyModifierNode(
    override var node: Modifier.Node,
    val root: GuiyNode,
) : ModifierWrapper<GuiyModifierNode>, Measurable, Placeable.PlacementScope, MeasureScope {
    override var next: GuiyModifierNode? = null
    private var measureResult: MeasureResult = NotMeasured
    override var x: Int = 0
    override var y: Int = 0
    val width get() = placeable.width
    val height get() = placeable.height

    override val parentData: Any?
        get() {
            val node = node
            return if (node is ParentDataModifierNode) {
                node.run { modifyParentData(next?.parentData) }
            } else next?.parentData
        }


    private val placeable = object : Placeable() {
        override var width: Int = 0
        override var height: Int = 0

        override fun placeAt(x: Int, y: Int) {
            this@GuiyModifierNode.x = x
            this@GuiyModifierNode.y = y
            measureResult.placeChildren()
        }
    }

    fun isInBounds(event: ClickEvent) =
        (event.globalPosition.x in x until (x + width) && event.globalPosition.y in y until (y + height))

    override fun measure(constraints: Constraints): Placeable {
        val node = node
        val result = when {
            next == null -> root.measurePolicy.run {
                this@GuiyModifierNode.measure(
                    root.children.map { it.layoutDelegate },
                    constraints
                )
            }

            node is LayoutModifierNode -> {
                node.run { this@GuiyModifierNode.measure(next!!, constraints) }
            }

            else -> {
                val placeable = next!!.measure(constraints)
                object : MeasureResult {
                    override val width: Int = placeable.width
                    override val height: Int = placeable.height
                    override fun placeChildren() {
                        placeable.place(0, 0)
                    }
                }
            }
        }
        measureResult = result
        placeable.width = result.width
        placeable.height = result.height
        if (node is LayoutAwareModifierNode) {
            node.onRemeasured(IntSize(result.width, result.height))
        }
        return placeable
    }

    fun placeAt(x: Int, y: Int) {
        this.x = x
        this.y = y
        val node = node
        if (node is LayoutAwareModifierNode) {
            node.onPlaced(IntOffset(x, y))
        }
        measureResult.placeChildren()
    }

    fun drawTo(scope: GuiyNodeDrawScope) {
        scope.drawNode = this
        scope.drawContent()
    }

    fun processClickEvent(event: ClickEvent): Boolean {
        if (!isInBounds(event)) return false
        if (next == null) return root.children.any { it.layoutDelegate.processClickEvent(event) }
        val node = node
        if (node !is InteractionModifierNode) return next!!.processClickEvent(event)
        event.position = event.globalPosition - IntOffset(x, y)
        node.onEvent(event)
        if (event.isConsumed) return true
        return next!!.processClickEvent(event)
    }
}

internal class GuiyNodeDrawScope() : ContentDrawScope {
    override var width: Int = 0
    override var height: Int = 0
    var x = 0
    var y = 0
    var canvas: InventoryCanvas? = null
    var drawNode: GuiyModifierNode? = null

    override fun drawItem(x: Int, y: Int, itemStack: ItemStack?) {
        val canvas = canvas ?: return
        canvas.set(this.x + x, this.y + y, itemStack)
    }

    override fun drawItemRect(offset: IntOffset, size: IntSize, itemStack: ItemStack?) {
        for (x in offset.x until (offset.x + size.width))
            for (y in offset.y until (offset.y + size.height))
                drawItem(x = x, y = y, itemStack = itemStack)
    }

    override fun drawContent() {
        val node = drawNode ?: return
        val next = node.next ?: return node.root.children.forEach { it.layoutDelegate.drawTo(this) }
        val drawNode = node.node
        this.drawNode = next
        if (drawNode is DrawModifierNode) {
            draw(drawNode.overrideCanvas, IntOffset(node.x, node.y), IntSize(node.width, node.height)) {
                drawNode.run { draw() }
            }
        } else {
            drawContent()
        }
        this.drawNode = node
    }

    inline fun draw(
        overrideCanvas: InventoryCanvas? = null,
        offset: IntOffset,
        size: IntSize,
        block: ContentDrawScope.() -> Unit,
    ) {
        val prevOffset = IntOffset(x, y)
        val prevSize = IntSize(width, height)
        val prevCanvas = canvas
        x = offset.x
        y = offset.y
        width = size.width
        height = size.height
        if (overrideCanvas != null) canvas = overrideCanvas
        try {
            block()
        } finally {
            canvas = prevCanvas
            x = prevOffset.x
            y = prevOffset.y
            width = prevSize.width
            height = prevSize.height
        }
    }

}