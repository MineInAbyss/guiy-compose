package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.ColumnMeasurer
import com.mineinabyss.guiy.nodes.ColumnPlacer
import com.mineinabyss.guiy.nodes.RowMeasurer
import com.mineinabyss.guiy.nodes.RowPlacer

@Composable
fun Row(modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Layout(
        RowMeasurer,
        RowPlacer,
        modifier = modifier,
        content = children
    )
}

@Composable
fun Column(modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Layout(
        ColumnMeasurer,
        ColumnPlacer,
        modifier = modifier,
        content = children
    )
}
