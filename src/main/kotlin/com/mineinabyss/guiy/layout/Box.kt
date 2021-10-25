package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.components.GuiyUIScopeMarker
import com.mineinabyss.guiy.modifiers.ConstraintsModifier
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.*
import com.mineinabyss.guiy.nodes.GuiyNodeApplier

//@GuiyUIScopeMarker
//interface BoxScope {
//    fun Modifier.constrain(
//        minWidth: Int = 0,
//        maxWidth: Int = Integer.MAX_VALUE,
//        minHeight: Int = 0,
//        maxHeight: Int = Integer.MAX_VALUE
//    ) = then(ConstraintsModifier(minWidth, maxWidth, minHeight, maxHeight))
//}

@Composable
fun Row(modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Layout(RowMeasurer, RowPlacer, modifier, children)
}

@Composable
fun Column(modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Layout(ColumnMeasurer, ColumnPlacer, modifier, children)
}
