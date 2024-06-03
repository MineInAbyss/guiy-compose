package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.jetpack.Alignment
import com.mineinabyss.guiy.jetpack.Arrangement
import com.mineinabyss.guiy.jetpack.LayoutDirection
import com.mineinabyss.guiy.modifiers.Modifier

/**
 * A layout component that places contents in a column top-to-bottom.
 */
@Composable
fun Column(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
) {
    val measurePolicy = remember(verticalArrangement, horizontalAlignment) {
        ColumnMeasurePolicy(
            verticalArrangement,
            horizontalAlignment
        )
    }
    Layout(
        measurePolicy,
        modifier = modifier,
        content = content
    )
}

private data class ColumnMeasurePolicy(
    private val verticalArrangement: Arrangement.Vertical,
    private val horizontalAlignment: Alignment.Horizontal,
) : RowColumnMeasurePolicy(
    sumHeight = true,
    arrangementSpacing = verticalArrangement.spacing
) {
    override fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult {
        val positions = IntArray(placeables.size)
        verticalArrangement.arrange(
            totalSize = height,
            sizes = placeables.map { it.height }.toIntArray(),
            outPositions = positions
        )
        return MeasureResult(width, height) {
            var childY = 0
            placeables.forEachIndexed { index, child ->
                child.placeAt(horizontalAlignment.align(child.height, height, LayoutDirection.Ltr), positions[index])
                childY += child.height
            }
        }
    }
}
