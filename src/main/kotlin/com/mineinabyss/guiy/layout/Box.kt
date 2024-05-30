package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.ColumnMeasurePolicy
import com.mineinabyss.guiy.nodes.RowMeasurePolicy

/**
 * A layout component that places contents in a row left-to-right.
 */
@Composable
fun Row(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        RowMeasurePolicy,
        modifier = modifier,
        content = content
    )
}

/**
 * A layout component that places contents in a column top-to-bottom.
 */
@Composable
fun Column(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        ColumnMeasurePolicy,
        modifier = modifier,
        content = content
    )
}
