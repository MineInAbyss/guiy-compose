package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Stable
import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.modifiers.Constraints
import com.mineinabyss.guiy.nodes.GuiyNode

data class MeasureResult(
    val width: Int,
    val height: Int,
    val placer: Placer,
)

@Stable
fun interface MeasurePolicy {
    fun measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult
}

@Stable
fun interface Placer {
    fun placeChildren()
}

@Stable
fun interface Renderer {
    fun GuiyCanvas.render(node: GuiyNode)
}

interface Measurable {
    fun measure(constraints: Constraints): Placeable
}

interface Placeable {
    var width: Int
    var height: Int

    fun placeAt(x: Int, y: Int)
}
