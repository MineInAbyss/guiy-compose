package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.mineinabyss.guiy.modifiers.drawBehind
import com.mineinabyss.idofront.textcomponents.miniMsg
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemLore
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.dvyy.compose.mini.layout.Layout
import me.dvyy.compose.mini.layout.modifiers.sizeIn
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * An item to display in an inventory layout.
 *
 * @param itemStack The [ItemStack] to display.
 */
@Composable
fun Item(itemStack: ItemStack?, modifier: Modifier = Modifier) {
    Layout(
        measurePolicy = { _, constraints -> layout(constraints.minWidth, constraints.minHeight) {} },
        modifier = modifier.drawBehind {
            for (x in 0 until width)
                for (y in 0 until height)
                    drawItem(x, y, itemStack)
        }.sizeIn(minWidth = 1.dp, minHeight = 1.dp)
    )
}

/**
 * An item to display in an inventory layout.
 *
 * @param material The [Material] of the item.
 * @param title The item's display name (formatted by MiniMesssage).
 * @param amount The amount of the item.
 * @param lore The item's lore (formatted by MiniMessage).
 */
@Composable
fun Item(
    material: Material,
    title: String? = null,
    amount: Int = 1,
    lore: List<String> = listOf(),
    hideTooltip: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val titleMM = remember(title) { title?.miniMsg() }
    val loreMM = remember(lore) { lore.map { it.miniMsg() } }

    val item = remember(material, title, amount, lore, hideTooltip) {
        ItemStack.of(material, amount).apply {
            if (titleMM != null) setData(DataComponentTypes.ITEM_NAME, titleMM)
            setData(DataComponentTypes.LORE, ItemLore.lore(loreMM))
            if (hideTooltip) setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true))
        }
    }

    Item(item, modifier)
}


