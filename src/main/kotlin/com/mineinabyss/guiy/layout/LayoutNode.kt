package com.mineinabyss.guiy.layout

import com.mineinabyss.guiy.canvas.GuiyCanvas
import com.mineinabyss.guiy.canvas.ClickResult
import com.mineinabyss.guiy.components.state.IntOffset
import com.mineinabyss.guiy.components.state.IntSize
import com.mineinabyss.guiy.modifiers.Constraints
import com.mineinabyss.guiy.modifiers.LayoutChangingModifier
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.OnSizeChangedModifier
import com.mineinabyss.guiy.modifiers.click.ClickModifier
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.nodes.GuiyNode
import kotlin.reflect.KClass

/**
 * TODO structure is really not decided on yet.
 *  I'd really like to avoid inheritance, and have only one ComposableNode call that creates Layout.
 *  You can configure stuff through [measurePolicy], [placer], and the [modifier], but things creates some problems
 *  when trying to make your own composable nodes that interact with this Layout node.
 */
class LayoutNode : Measurable, Placeable, GuiyNode {
    override var measurePolicy: MeasurePolicy = ChildMeasurePolicy
    override var renderer: Renderer = EmptyRenderer
    override var canvas: GuiyCanvas? = null

    override var modifier: Modifier = Modifier
        set(value) {
            field = value
            processedModifier = modifier.foldIn(mutableMapOf()) { acc, element ->
                val existing = acc[element::class]
                if (existing != null)
                    acc[element::class] = existing.unsafeMergeWith(element)
                else
                    acc[element::class] = element
                acc
            }
            layoutChangingModifiers = modifier.foldIn(mutableListOf()) { acc, element ->
                if (element is LayoutChangingModifier) acc.add(element)
                acc
            }
        }
    var processedModifier = mapOf<KClass<out Modifier.Element<*>>, Modifier.Element<*>>()
    var layoutChangingModifiers: List<LayoutChangingModifier> = emptyList()

    inline fun <reified T : Modifier.Element<T>> get(): T? {
        return processedModifier[T::class] as T?
    }

    var parent: LayoutNode? = null
    val children = mutableListOf<LayoutNode>()

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
        val innerConstraints = layoutChangingModifiers.fold(constraints) { inner, modifier ->
            modifier.modifyInnerConstraints(inner)
        }
        val result = measurePolicy.measure(children, innerConstraints)

        if (width != result.width || height != result.height) {
            get<OnSizeChangedModifier>()?.onSizeChanged?.invoke(Size(result.width, result.height))
        }
        width = result.width
        height = result.height
        result.placer.placeChildren()

        val layoutConstraints = layoutChangingModifiers.fold(constraints) { outer, modifier ->
            modifier.modifyLayoutConstraints(IntSize(result.width, result.height), outer)
        }
        // Returned constraints will always appear as though they are in parent's bounds
        return coercedConstraints(layoutConstraints)
    }

    override fun placeAt(x: Int, y: Int) {
        val offset = layoutChangingModifiers.fold(IntOffset(x, y)) { acc, modifier ->
            modifier.modifyPosition(acc)
        }
        this.x = offset.x
        this.y = offset.y
    }

    override fun renderTo(guiyCanvas: GuiyCanvas?) {
        val offsetCanvas = (guiyCanvas ?: this.canvas)?.subCanvas(x, y)
        renderer.apply { offsetCanvas?.render(this@LayoutNode) }
        children.forEach { it.renderTo(offsetCanvas) }
        renderer.apply { offsetCanvas?.renderAfterChildren(this@LayoutNode) }
    }

    fun processClick(scope: ClickScope, x: Int, y: Int): ClickResult {
        val childResult = children
            .filter { x in it.x until (it.x + it.width) && y in it.y until (it.y + it.height) }
            .foldRight(ClickResult()) { it, acc ->
                val processed = it.processClick(scope, x - it.x, y - it.y)
                if (processed.clickConsumed == true) {
                    return processed
                }
                acc.mergeWith(processed)
            }

        if (childResult.clickConsumed != true) get<ClickModifier>()?.run {
            onClick.invoke(scope)
            if (consumeClick) return ClickResult(true)
        }

        return childResult
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

val EmptyRenderer = object : Renderer {}