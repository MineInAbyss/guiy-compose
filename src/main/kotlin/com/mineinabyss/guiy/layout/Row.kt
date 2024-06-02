package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.layout.alignment.Alignment
import com.mineinabyss.guiy.modifiers.Modifier

/**
 * A layout component that places contents in a row left-to-right.
 */
@Composable
fun Row(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) {
    val measurePolicy = remember(verticalAlignment) { RowMeasurePolicy(verticalAlignment) }
    Layout(
        measurePolicy,
        modifier = modifier,
        content = content
    )
}

private data class RowMeasurePolicy(
    private val verticalAlignment: Alignment.Vertical,
) : RowLikeMeasurePolicy() {
    override fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult {
        return MeasureResult(width, height) {
            var childX = 0
            for (child in placeables) {
                child.placeAt(childX, verticalAlignment.align(child.height, height))
                childX += child.width
            }
        }
    }
}
