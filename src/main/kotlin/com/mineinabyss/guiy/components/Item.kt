package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.nodes.BoxNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier

@Composable
fun Box(width: Int, height: Int, children: @Composable () -> Unit) {
    ComposeNode<BoxNode, GuiyNodeApplier>(
        factory = ::BoxNode,
        update = {
            println("Updating")
            set(width) { this.width = width }
            set(height) { this.height = height }
        },
        content = children
    )
}

//@Composable
//fun Item(itemStack: ItemStack) {
//    ComposeNode<BoxNode, GuiyNodeApplier>(
//        factory = ::BoxNode,
//        update = { }
//    )
//}
