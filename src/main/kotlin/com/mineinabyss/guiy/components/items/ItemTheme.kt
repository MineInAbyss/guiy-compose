package com.mineinabyss.guiy.components.items

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.TooltipDisplay
import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Immutable
class ItemTheme(
    val invisible: ItemStack = ItemStack.of(Material.PAPER).apply {
        setData(DataComponentTypes.ITEM_MODEL, Key.key("minecraft:empty"))
    },
    val hiddenTooltip: ItemStack = invisible.clone().apply {
        setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true))
    },
)

internal val DefaultItemTheme = ItemTheme()

val LocalItemTheme = staticCompositionLocalOf<ItemTheme> { DefaultItemTheme }
