package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.canvas.LocalCanvas
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.GuiyNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier

/**
 * The main component for layout, it measures and positions zero or more children.
 */
@Composable
inline fun Layout(
    measurePolicy: MeasurePolicy,
    renderer: Renderer = EmptyRenderer,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val canvas = LocalCanvas.current
    ComposeNode<GuiyNode, GuiyNodeApplier>(
        factory = GuiyNode.Constructor,
        update = {
            set(measurePolicy) { this.measurePolicy = it }
            set(renderer) { this.renderer = it }
            set(canvas) { this.canvas = it }
            set(modifier) { this.modifier = it }
        },
        content = content,
    )
}
