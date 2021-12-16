package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.GuiyNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier

@Composable
inline fun Layout(
    measurePolicy: MeasurePolicy,
    renderer: Renderer = EmptyRenderer,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    ComposeNode<GuiyNode, GuiyNodeApplier>(
        factory = GuiyNode.Constructor,
        update = {
            set(measurePolicy) { this.measurePolicy = it }
            set(renderer) { this.renderer = it }
            set(modifier) { this.modifier = it }
        },
        content = content,
    )
}
