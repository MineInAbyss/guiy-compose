package com.mineinabyss.guiy.modifiers.placement.offset

import com.mineinabyss.guiy.components.state.IntOffset
import com.mineinabyss.guiy.layout.Placeable

class OffsetPlaceable(
    val offset: IntOffset,
    val inner: Placeable
) : Placeable by inner {
    override fun placeAt(x: Int, y: Int) {
    }
}
