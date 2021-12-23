package com.mineinabyss.guiy.modifiers

class PositionModifier(
    val x: Int = 0,
    val y: Int = 0,
) : Modifier.Element<PositionModifier> {
    override fun mergeWith(other: PositionModifier) = other
}

fun Modifier.at(x: Int = 0, y: Int = 0) = then(PositionModifier(x, y))
