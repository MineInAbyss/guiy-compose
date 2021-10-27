package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.layout.Measurer
import com.mineinabyss.guiy.modifiers.Modifier
import kotlin.math.max

@Composable
fun staticMeasurer(width: Int, height: Int) = remember(width, height) {
    Measurer { children ->
        children.forEach { it.measure() }
        MeasureResult(width, height)
    }
}

@Composable
fun Grid(width: Int, height: Int, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        measurer = staticMeasurer(width, height),
        placer = { children ->
            var placeAtX = 0
            var placeAtY = 0
            var maxHeight = 0
            for (child in children) {
                child.placeAt(placeAtX, placeAtY)
                placeAtX += child.width
                maxHeight = max(maxHeight, child.height)
                child.placeChildren()

                if (placeAtX >= width) {
                    placeAtX = 0
                    placeAtY += maxHeight
                    maxHeight = 0
                }
            }
        },
        modifier = modifier,
        content = content
    )
}
