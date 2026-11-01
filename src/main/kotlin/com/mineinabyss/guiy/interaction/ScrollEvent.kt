package com.mineinabyss.guiy.interaction

import androidx.compose.ui.unit.IntOffset
import org.bukkit.entity.Player

data class ScrollEvent(
    /** 1 when the wheel moves down, -1 when it moves up */
    val delta: Int,
    val whoScrolled: Player,
    val globalPosition: IntOffset,
) {
    /** Local position of the scroll, relative to the current node's top left corner */
    var position: IntOffset = globalPosition
        internal set
    var isConsumed = false

    companion object {
        fun fromSlot(delta: Int, slot: Int, whoScrolled: Player) = ScrollEvent(
            delta = delta,
            whoScrolled = whoScrolled,
            globalPosition = IntOffset(slot % 9, slot / 9),
        )
    }
}
