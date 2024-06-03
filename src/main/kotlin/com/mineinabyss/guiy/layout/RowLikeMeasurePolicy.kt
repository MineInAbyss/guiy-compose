package com.mineinabyss.guiy.layout

import com.mineinabyss.guiy.modifiers.Constraints
import kotlin.math.max

abstract class RowLikeMeasurePolicy(
    val sumWidth: Boolean = false,
    val sumHeight: Boolean = false,
) : MeasurePolicy {
    override fun measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val noMinConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(noMinConstraints) }
        val width = if (sumWidth) placeables.sumOf { it.width } else placeables.maxOfOrNull { it.width } ?: 0
        val height = if (sumHeight) placeables.sumOf { it.height } else placeables.maxOfOrNull { it.height } ?: 0
        return placeChildren(placeables, max(width, constraints.minWidth), max(height, constraints.minHeight))
    }

    abstract fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult
}
