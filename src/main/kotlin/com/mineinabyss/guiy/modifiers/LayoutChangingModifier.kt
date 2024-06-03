package com.mineinabyss.guiy.modifiers

import com.mineinabyss.guiy.components.state.IntOffset
import com.mineinabyss.guiy.components.state.IntSize

interface LayoutChangingModifier {
    fun modifyPosition(offset: IntOffset): IntOffset = offset

    /** Modify constraints as they appear to parent nodes laying out this node. */
    fun modifyLayoutConstraints(measuredSize: IntSize, constraints: Constraints): Constraints =
        modifyInnerConstraints(constraints)

    /** Modify constraints as they appear to this node and its children for layout. */
    fun modifyInnerConstraints(constraints: Constraints): Constraints = constraints
}
