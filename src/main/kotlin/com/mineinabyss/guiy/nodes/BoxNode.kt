package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.layout.Measurable
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.layout.Measurer
import com.mineinabyss.guiy.layout.Placer
import com.mineinabyss.guiy.modifiers.getConstraints

val RowMeasurer = Measurer { children ->
    var width = 0
    var height = 0
    for (child in children) {
        child.measure()
        width += child.width
        height = maxOf(height, child.height)
    }

    MeasureResult(width, height)
}

val ColumnMeasurer = Measurer { children ->
    var width = 0
    var height = 0
    for (child in children) {
        child.measure()
        width = maxOf(width, child.width)
        height += child.height
    }

    MeasureResult(width, height)
}

val RowPlacer = Placer { children ->
    var childX = 0
    for (child in children) {
        child.placeAt(childX, 0)
        child.placeChildren()
        childX += child.width
    }
}

val ColumnPlacer = Placer { children ->
    var childX = 0
    for (child in children) {
        child.placeAt(childX, 0)
        child.placeChildren()
        childX += child.width
    }
    var childY = 0
    for (child in children) {
        child.placeAt(0, childY)
        child.placeChildren()
        childY += child.height
    }
}
