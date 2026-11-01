package com.mineinabyss.guiy.components.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.rememberMiniMsg
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemLore
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.dvyy.compose.mini.modifier.Modifier
import net.kyori.adventure.text.Component

/**
 * Text with invisible item, use [LocalItemTheme] to choose the invisible item based on your resourcepack.
 */
@Composable
fun Text(name: String, vararg lore: String, hideTooltip: Boolean = false, modifier: Modifier = Modifier) {
    val mmName = rememberMiniMsg(name)
    val mmLore = rememberMiniMsg(*lore).toTypedArray()
    Text(mmName, lore = mmLore, hideTooltip, modifier)
}

/**
 * Text with invisible item, use [LocalItemTheme] to choose the invisible item based on your resourcepack.
 */
@Composable
fun Text(name: Component, vararg lore: Component, hideTooltip: Boolean = false, modifier: Modifier = Modifier) {
    val invisible = LocalItemTheme.current.invisible
    val item = remember(name, lore, invisible, hideTooltip) {
        invisible.clone().apply {
            setData(DataComponentTypes.ITEM_NAME, name)
            setData(DataComponentTypes.LORE, ItemLore.lore(lore.toList()))
            if (hideTooltip) setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true))
        }
    }
    Item(item, modifier)
}
