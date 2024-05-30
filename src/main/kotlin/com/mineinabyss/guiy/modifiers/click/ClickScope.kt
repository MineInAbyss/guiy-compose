package com.mineinabyss.guiy.modifiers.click

import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

data class ClickScope(
    val clickType: ClickType,
    val slot: Int,
    val cursor: ItemStack?,
    val whoClicked: HumanEntity
)
