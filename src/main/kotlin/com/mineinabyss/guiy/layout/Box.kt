package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.state.IntSize
import com.mineinabyss.guiy.jetpack.Alignment
import com.mineinabyss.guiy.jetpack.LayoutDirection
import com.mineinabyss.guiy.modifiers.Modifier

@Composable
fun Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    val measurePolicy = remember(contentAlignment) { BoxMeasurePolicy(contentAlignment) }
    Layout(
        measurePolicy,
        modifier = modifier,
        content = content
    )
}

internal data class BoxMeasurePolicy(
    private val alignment: Alignment,
) : RowColumnMeasurePolicy() {
    override fun placeChildren(placeables: List<Placeable>, width: Int, height: Int): MeasureResult {
        return MeasureResult(width, height) {
            for (child in placeables) {
                child.placeAt(alignment.align(child.size, IntSize(width, height), LayoutDirection.Ltr))
            }
        }
    }
}
