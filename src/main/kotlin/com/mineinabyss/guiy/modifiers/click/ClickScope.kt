package com.mineinabyss.guiy.modifiers.click

import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

data class ClickScope(
    val clickType: ClickType,
    val x: Int,
    val y: Int,
    val whoClicked: HumanEntity,
    val cursor: ItemStack? = whoClicked.itemOnCursor
) {
    fun offset(ox: Int, oy: Int) : ClickScope {
        return ClickScope(
            clickType = clickType,
            x = x + ox,
            y = y + oy,
            whoClicked = whoClicked
        )
    }

    constructor(
        clickType: ClickType,
        slot: Int,
        whoClicked: HumanEntity,
        cursor: ItemStack? = whoClicked.itemOnCursor,
    ) : this(
        clickType = clickType,
        x = slot % 9,
        y = slot / 9,
        whoClicked = whoClicked,
        cursor = cursor
    )
}
