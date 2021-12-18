package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.height
import com.mineinabyss.guiy.modifiers.width

@Composable
fun Spacer(modifier: Modifier = Modifier) {
    Layout(
        measurePolicy = { measurables, constraints ->
            MeasureResult(constraints.minWidth, constraints.minHeight) {}
        },
        modifier = modifier,
    )
}

@Composable
fun Spacer(width: Int? = null, height: Int? = null, modifier: Modifier = Modifier) {
    Spacer(modifier
        .run { if (width != null) width(width) else this }
        .run { if (height != null) height(height) else this }
    )
}
