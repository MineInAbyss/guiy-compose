package com.mineinabyss.guiy.modifiers.placement.offset

import androidx.compose.runtime.Stable
import com.mineinabyss.guiy.components.state.IntOffset
import com.mineinabyss.guiy.modifiers.LayoutChangingModifier
import com.mineinabyss.guiy.modifiers.Modifier

data class OffsetModifier(
    val offset: IntOffset
) : Modifier.Element<OffsetModifier>, LayoutChangingModifier {
    override fun mergeWith(other: OffsetModifier) = other

    override fun modifyPosition(offset: IntOffset): IntOffset = offset + this.offset
}

@Stable
fun Modifier.offset(x: Int, y: Int) = then(OffsetModifier(IntOffset(x, y)))
