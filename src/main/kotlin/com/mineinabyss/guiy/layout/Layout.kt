package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.GuiyNodeApplier

val ChildPlacer = Placer { children -> children.forEach { it.placeChildren() } }

@Composable
fun Layout(
    measurer: Measurer? = null,
    placer: Placer = ChildPlacer,
    renderer: Renderer = LayoutNode.EmptyRenderer,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    ComposeNode<LayoutNode, GuiyNodeApplier>(
        factory = ::LayoutNode,
        update = {
            if(measurer != null)
                set(measurer) { this.measurer = it }
            set(placer) { this.placer = it }
            set(renderer) { this.renderer = it }
            set(modifier) { this.modifier = it }
        },
        content = content,
    )
}
