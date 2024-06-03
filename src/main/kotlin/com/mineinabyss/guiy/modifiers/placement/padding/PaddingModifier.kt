package com.mineinabyss.guiy.modifiers.placement.padding

import androidx.compose.runtime.Stable
import com.mineinabyss.guiy.components.state.IntOffset
import com.mineinabyss.guiy.modifiers.Constraints
import com.mineinabyss.guiy.modifiers.LayoutChangingModifier
import com.mineinabyss.guiy.modifiers.Modifier
import kotlin.math.max

data class PaddingModifier(
    val padding: PaddingValues
) : Modifier.Element<PaddingModifier>, LayoutChangingModifier {
    override fun mergeWith(other: PaddingModifier) = PaddingModifier(
        PaddingValues(
            max(padding.start, other.padding.start),
            max(padding.end, other.padding.end),
            max(padding.top, other.padding.top),
            max(padding.bottom, other.padding.bottom),
        )
    )

    override fun modifyPosition(offset: IntOffset): IntOffset = offset + padding.getOffset()

    override fun modifyInnerConstraints(constraints: Constraints) = constraints.copy(
        maxWidth = constraints.maxWidth - padding.end - padding.start,
        maxHeight = constraints.maxHeight - padding.bottom - padding.top,
    )
}

@Stable
fun Modifier.padding(
    start: Int = 0,
    end: Int = 0,
    top: Int = 0,
    bottom: Int = 0,
) = then(PaddingModifier(PaddingValues(start, end, bottom, top)))

@Stable
fun Modifier.padding(horizontal: Int = 0, vertical: Int = 0) =
    padding(horizontal, horizontal, vertical, vertical)
