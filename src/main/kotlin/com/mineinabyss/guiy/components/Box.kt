package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.nodes.BoxNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier

@Composable
private fun Box(isRow: Boolean, children: @Composable () -> Unit) {
    ComposeNode<BoxNode, GuiyNodeApplier>(
        factory = ::BoxNode,
        update = {
            set(isRow) {
                this.isRow = isRow
            }
        },
        content = children,
    )
}

@Composable
fun Row(children: @Composable () -> Unit) {
    Box(true, children)
}

@Composable
fun Column(children: @Composable () -> Unit) {
    Box(false, children)
}
