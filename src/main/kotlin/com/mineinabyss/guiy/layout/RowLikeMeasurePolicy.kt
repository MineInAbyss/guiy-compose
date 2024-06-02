package com.mineinabyss.guiy.layout

import com.mineinabyss.guiy.modifiers.Constraints

abstract class RowLikeMeasurePolicy : MeasurePolicy {
    override fun measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val noMinConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(noMinConstraints) }
        val width = placeables.maxOfOrNull { it.width } ?: 0
        val height = placeables.maxOfOrNull { it.height } ?: 0
        return placeChildren(placeables, width, height)
    }

    abstract fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult
}
