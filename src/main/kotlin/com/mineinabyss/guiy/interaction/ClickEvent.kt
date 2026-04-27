package com.mineinabyss.guiy.interaction

import androidx.compose.ui.unit.IntOffset
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

data class ClickEvent(
    val clickType: ClickType,
    val whoClicked: HumanEntity,
    val cursor: ItemStack? = whoClicked.itemOnCursor,
    val globalPosition: IntOffset,
) {
    /** Local position of the click (relative to the current node's top left corner.) */
    var position: IntOffset = globalPosition
        internal set
    var isConsumed = false

    companion object {
        fun fromSlot(
            clickType: ClickType,
            slot: Int,
            whoClicked: HumanEntity,
            cursor: ItemStack? = whoClicked.itemOnCursor,
        ) = ClickEvent(
            clickType = clickType,
            globalPosition = IntOffset(slot % 9, slot / 9),
            whoClicked = whoClicked,
            cursor = cursor
        )
    }
}