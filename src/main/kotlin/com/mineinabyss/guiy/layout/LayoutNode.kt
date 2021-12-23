package com.mineinabyss.guiy.layout

import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.inventory.OffsetCanvas
import com.mineinabyss.guiy.modifiers.*
import com.mineinabyss.guiy.nodes.GuiyNode
import org.bukkit.event.inventory.ClickType
import kotlin.reflect.KClass

/**
 * TODO structure is really not decided on yet.
 *  I'd really like to avoid inheritance, and have only one ComposableNode call that creates Layout.
 *  You can configure stuff through [measurePolicy], [placer], and the [modifier], but things creates some problems
 *  when trying to make your own composable nodes that interact with this Layout node.
 */
internal class LayoutNode : Measurable, Placeable, GuiyNode {
    override var measurePolicy: MeasurePolicy = ChildMeasurePolicy
    override var renderer: Renderer = EmptyRenderer
    override var canvas: GuiyCanvas? = null

    val children = mutableListOf<LayoutNode>()
    override var modifier: Modifier = Modifier
        set(value) {
            field = value
            processedModifier = modifier.foldOut(mutableMapOf()) { element, acc ->
                val existing = acc[element::class]
                if (existing != null)
                    acc[element::class] = element.unsafeMergeWith(existing)
                else
                    acc[element::class] = element
                acc
            }
        }
    var processedModifier = mapOf<KClass<out Modifier.Element<*>>, Modifier.Element<*>>()

    inline fun <reified T : Modifier.Element<T>> get(): T? {
        return processedModifier[T::class] as T?
    }

    var parent: LayoutNode? = null

    override var width: Int = 0
    override var height: Int = 0
    override var x: Int = 0
    override var y: Int = 0

    private fun coercedConstraints(constraints: Constraints) = with(constraints) {
        object : Placeable by this@LayoutNode {
            override var width: Int = this@LayoutNode.width.coerceIn(minWidth..maxWidth)
            override var height: Int = this@LayoutNode.height.coerceIn(minHeight..maxHeight)
        }
    }

    override fun measure(constraints: Constraints): Placeable {
        val modifierConstraints =
            (get<SizeModifier>()
                ?.let { SizeModifier(constraints).mergeWith(it).constraints }
                ?: constraints)
                .applyFill(get<HorizontalFillModifier>(), get<VerticalFillModifier>())
        val result = measurePolicy.measure(children, modifierConstraints)

        if (width != result.width || height != result.height) {
            get<OnSizeChangedModifier>()?.onSizeChanged?.invoke(Size(result.width, result.height))
        }
        width = result.width
        height = result.height
        result.placer.placeChildren()

        // Returned constraints will always appear as though they are in parent's bounds
        return coercedConstraints(constraints)
    }

    override fun placeAt(x: Int, y: Int) {
        val absolute = get<PositionModifier>()
        if (absolute != null) {
            this.x = absolute.x
            this.y = absolute.y
        } else {
            this.x = x
            this.y = y
        }
    }

    override fun renderTo(canvas: GuiyCanvas?) {
        val offsetCanvas = (canvas ?: this.canvas)?.let { OffsetCanvas(x, y, it) }
        renderer.apply { offsetCanvas?.render(this@LayoutNode) }
        for (child in children) child.renderTo(offsetCanvas)
    }

    fun processClick(scope: ClickScope, x: Int, y: Int, type: ClickType) {
        get<ClickModifier>()?.onClick?.invoke(scope)
        children.filter { x in it.x until (it.x + it.width) && y in it.y until (it.y + it.height) }
            .forEach { it.processClick(scope, x - it.x, y - it.y, type) }
    }

    override fun toString() = children.joinToString(prefix = "LayoutNode(", postfix = ")")

    internal companion object {
        val ChildMeasurePolicy = MeasurePolicy { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            MeasureResult(placeables.maxOfOrNull { it.width } ?: 0, placeables.maxOfOrNull { it.height } ?: 0) {
                placeables.forEach { it.placeAt(0, 0) }
            }
        }
        private val ErrorMeasurePolicy = MeasurePolicy { _, _ -> error("Measurer not defined") }
    }
}

val EmptyRenderer = Renderer { }
