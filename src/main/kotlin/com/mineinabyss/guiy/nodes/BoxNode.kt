package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.layout.MeasurePolicy
import com.mineinabyss.guiy.layout.MeasureResult

val RowMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val noMinConstraints = constraints.copy(minWidth = 0, minHeight = 0)
    val placeables = measurables.map { it.measure(noMinConstraints) }
    val width = placeables.sumOf { it.width }
    val height = placeables.maxOfOrNull { it.height } ?: 0

    MeasureResult(width, height) {
        var childX = 0
        for (child in placeables) {
            child.placeAt(childX, 0)
            childX += child.width
        }
    }
}

val ColumnMeasurePolicy = MeasurePolicy {  measurables, constraints ->
    val noMinConstraints = constraints.copy(minWidth = 0, minHeight = 0)
    val placeables = measurables.map { it.measure(noMinConstraints) }
    val width = placeables.maxOfOrNull { it.width } ?: 0
    val height = placeables.sumOf { it.height }

    MeasureResult(width, height) {
        var childY = 0
        for (child in placeables) {
            child.placeAt(0, childY)
            childY += child.height
        }
    }
}
