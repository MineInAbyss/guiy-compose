package com.mineinabyss.guiy.modifiers.placement.absolute

import com.mineinabyss.guiy.components.state.IntOffset
import com.mineinabyss.guiy.modifiers.LayoutChangingModifier
import com.mineinabyss.guiy.modifiers.Modifier

class PositionModifier(
    val x: Int = 0,
    val y: Int = 0,
) : Modifier.Element<PositionModifier>, LayoutChangingModifier {
    override fun mergeWith(other: PositionModifier) = other

    override fun modifyPosition(offset: IntOffset) = IntOffset(this.x, this.y)
}

/** Places an element at an absolute offset in the canvas. */
fun Modifier.at(x: Int = 0, y: Int = 0) = then(PositionModifier(x, y))
