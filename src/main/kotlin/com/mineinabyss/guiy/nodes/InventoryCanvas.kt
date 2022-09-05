package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.layout.MeasurePolicy
import com.mineinabyss.guiy.layout.MeasureResult

interface InventoryCloseScope {
    fun reopen()
}

interface InventoryOpenScope {
    fun open()
}

val StaticMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val noMinConstraints = constraints.copy(minWidth = 0, minHeight = 0)
    val placeables = measurables.map { it.measure(noMinConstraints) }
    MeasureResult(constraints.minWidth, constraints.minHeight) {
        placeables.forEach { it.placeAt(0, 0) }
    }
}
