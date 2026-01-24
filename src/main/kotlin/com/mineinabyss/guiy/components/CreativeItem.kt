package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.idofront.messaging.error
import org.bukkit.GameMode
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

/**
 * An item that acts like a creative inventory item that can be copied on click.
 */
@Composable
fun CreativeItem(
    itemStack: ItemStack?, modifier: Modifier = Modifier,
) {
    Item(itemStack, modifier.clickable {
        if (whoClicked.gameMode != GameMode.CREATIVE) {
            whoClicked.error("This item can only be used in creative mode.")
            return@clickable
        }

        // Mimic all vanilla interactions
        val isCursorEmpty = cursor == null || cursor.isEmpty
        val result: ItemStack? = when {
            (clickType.isShiftClick || clickType == ClickType.MIDDLE) && isCursorEmpty ->
                itemStack?.asQuantity(itemStack.maxStackSize)

            clickType == ClickType.MIDDLE -> return@clickable

            clickType == ClickType.SHIFT_LEFT && cursor != null && cursor.isSimilar(itemStack) ->
                cursor.asQuantity(cursor.maxStackSize)

            isCursorEmpty -> itemStack?.clone()?.asOne()

            clickType.isRightClick -> cursor.clone().subtract()

            clickType.isLeftClick && !cursor.isSimilar(itemStack) -> null

            else -> cursor.clone().add()
        }
        whoClicked.setItemOnCursor(result)
    })
}
