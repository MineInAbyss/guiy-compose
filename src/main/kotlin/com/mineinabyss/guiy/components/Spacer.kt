package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import me.dvyy.compose.mini.layout.Layout
import me.dvyy.compose.mini.layout.modifiers.height
import me.dvyy.compose.mini.layout.modifiers.width
import me.dvyy.compose.mini.modifier.Modifier

/**
 * A layout element that takes up space without drawing anything.
 */
@Composable
fun Spacer(modifier: Modifier = Modifier) {
    Layout(
        measurePolicy = { measurables, constraints ->
            layout(constraints.minWidth, constraints.minHeight) {}
        },
        modifier = modifier,
    )
}

/**
 * A layout element that takes up space without drawing anything.
 *
 * @param width The width of the spacer.
 * @param height The height of the spacer.
 */
@Composable
fun Spacer(width: Dp? = null, height: Dp? = null, modifier: Modifier = Modifier) {
    Spacer(
        modifier
        .run { if (width != null) width(width) else this }
        .run { if (height != null) height(height) else this }
    )
}
