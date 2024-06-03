package com.mineinabyss.guiy.modifiers

import com.mineinabyss.guiy.components.state.IntOffset

interface LayoutChangingModifier {
    fun modifyPosition(offset: IntOffset): IntOffset = offset

    /** Modify constraints as they appear to parent nodes laying out this node. */
    fun modifyLayoutConstraints(constraints: Constraints): Constraints = constraints

    /** Modify constraints as they appear to this node when laying out its children. */
    fun modifyInnerConstraints(constraints: Constraints): Constraints = modifyLayoutConstraints(constraints)
}
