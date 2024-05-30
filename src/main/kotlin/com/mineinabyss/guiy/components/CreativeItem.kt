package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.click.clickable
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

/**
 * An item that acts like a creative inventory item that can be copied on click.
 */
@Composable
fun CreativeItem(
    itemStack: ItemStack?, modifier: Modifier = Modifier
) {
    Item(itemStack, modifier.clickable {
        // Mimic all vanilla interactions
        val shiftClick = clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT
        val result: ItemStack? = when {
            (shiftClick || clickType == ClickType.MIDDLE) && cursor == null -> itemStack?.clone()
                ?.apply { amount = maxStackSize }

            clickType == ClickType.MIDDLE -> return@clickable

            (clickType == ClickType.SHIFT_LEFT && cursor != null && cursor.isSimilar(itemStack)) ->
                cursor.clone().apply { amount = maxStackSize }

            cursor == null -> itemStack?.clone()?.apply { amount = 1 }

            clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT -> cursor.clone().subtract()

            (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) && !cursor.isSimilar(itemStack) -> null

            else -> cursor.clone().add()
        }
        whoClicked.setItemOnCursor(result)
    })
}
