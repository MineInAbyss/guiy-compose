package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.MeasurePolicy
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.modifiers.Modifier

/**
 * A grid layout composable that arranges its children in a grid, left-to-right, top-to-bottom, wrapped
 * to the next row when its width is exceeded.
 */
@Composable
fun Grid(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        measurePolicy = GridMeasurePolicy,
        modifier = modifier,
        content = content
    )
}

val GridMeasurePolicy = MeasurePolicy { measurables, constraints ->
    val noMinConstraints = constraints.copy(minWidth = 0, minHeight = 0)
    val placeables = measurables.map { it.measure(noMinConstraints) }
    val itemWidth = placeables.maxOfOrNull { it.width } ?: 0
    val itemHeight = placeables.maxOfOrNull { it.height } ?: 0
    if (itemWidth == 0) return@MeasurePolicy MeasureResult(constraints.minWidth, constraints.minHeight) {}
    val width = constraints.maxWidth - (constraints.maxWidth % itemWidth)
    val height = (placeables.size / width * itemHeight).coerceIn(constraints.minHeight, constraints.maxHeight)

    MeasureResult(width, height) {
        var placeAtX = 0
        var placeAtY = 0
        for (child in placeables) {
            child.placeAt(placeAtX, placeAtY)
            placeAtX += itemWidth
            if (placeAtX >= width) {
                placeAtX = 0
                placeAtY += itemHeight
                if (placeAtY + itemHeight >= height) break
            }
        }
    }
}
