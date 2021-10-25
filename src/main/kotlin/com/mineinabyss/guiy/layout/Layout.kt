package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.GuiyNodeApplier

private val EmptyMeasurer = Measurer { MeasureResult(0, 0) }
private val EmptyPlacer = Placer { children -> children.forEach { it.placeChildren() } }

@Composable
fun Layout(
    measurer: Measurer = EmptyMeasurer,
    placer: Placer = EmptyPlacer,
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit
) {
    ComposeNode<LayoutNode, GuiyNodeApplier>(
        factory = ::LayoutNode,
        update = {
            set(measurer) { this.measurer = it }
            set(placer) { this.placer = it }
            set(modifier) { this.modifier = it }
        },
        content = children,
    )
}
