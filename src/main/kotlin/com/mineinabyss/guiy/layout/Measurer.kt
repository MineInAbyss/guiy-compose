package com.mineinabyss.guiy.layout

data class MeasureResult(
    val width: Int,
    val height: Int,
)

fun interface Measurer {
    fun measure(children: List<Measurable>): MeasureResult
}

fun interface Placer {
    fun placeChildren(children: List<Placeable>)
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
