package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.layout.alignment.Alignment
import com.mineinabyss.guiy.modifiers.Modifier

/**
 * A layout component that places contents in a column top-to-bottom.
 */
@Composable
fun Column(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
) {
    val measurePolicy = remember(horizontalAlignment) { ColumnMeasurePolicy(horizontalAlignment) }
    Layout(
        measurePolicy,
        modifier = modifier,
        content = content
    )
}

private data class ColumnMeasurePolicy(
    private val horizontalAlignment: Alignment.Horizontal,
) : RowLikeMeasurePolicy() {
    override fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult {
        return MeasureResult(width, height) {
            var childY = 0
            for (child in placeables) {
                child.placeAt(horizontalAlignment.align(child.height, height), childY)
                childY += child.height
            }
        }
    }
}
