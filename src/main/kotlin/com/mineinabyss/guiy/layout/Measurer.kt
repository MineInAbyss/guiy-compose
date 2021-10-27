package com.mineinabyss.guiy.layout

import androidx.compose.runtime.Stable
import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.nodes.GuiyNode

data class MeasureResult(
    val width: Int,
    val height: Int,
)

@Stable
fun interface Measurer {
    fun measure(children: List<Measurable>): MeasureResult
}

@Stable
fun interface Placer {
    fun placeChildren(children: List<Placeable>)
}

@Stable
fun interface Renderer {
    fun GuiyCanvas.render(node: GuiyNode)
}

interface Measurable {
    var width: Int
    var height: Int

    fun measure(): MeasureResult
}

interface Placeable {
    var width: Int
    var height: Int

    fun placeChildren()

    fun placeAt(x: Int, y: Int)
}
