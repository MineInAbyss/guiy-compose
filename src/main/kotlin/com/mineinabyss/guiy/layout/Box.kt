package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.ColumnMeasurePolicy
import com.mineinabyss.guiy.nodes.RowMeasurePolicy

@Composable
fun Row(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        RowMeasurePolicy,
        modifier = modifier,
        content = content
    )
}

@Composable
fun Column(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        ColumnMeasurePolicy,
        modifier = modifier,
        content = content
    )
}
