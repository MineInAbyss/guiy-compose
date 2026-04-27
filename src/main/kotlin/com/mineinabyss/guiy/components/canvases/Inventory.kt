package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import com.mineinabyss.guiy.canvas.inventory.GuiyInventory
import com.mineinabyss.guiy.canvas.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.canvas.inventory.InventoryCloseScope
import com.mineinabyss.guiy.draw.InventoryCanvas
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.modifiers.drawWithContent
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.modifier.Modifier
import net.kyori.adventure.text.Component
import org.bukkit.inventory.Inventory

val LocalInventory: ProvidableCompositionLocal<Inventory> =
    compositionLocalOf { error("No local inventory defined") }

/**
 * A layout composable that handles opening and closing an inventory for a set of players.
 *
 * @param inventory The bukkit inventory to be displayed.
 * @param viewers The set of players who will view the inventory.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun Inventory(
    inventory: Inventory,
    onClose: InventoryCloseScope.() -> Unit,
    title: Component? = null,
    modifier: Modifier = Modifier,
    gridToInventoryIndex: (IntOffset) -> Int?,
    inventoryIndexToGrid: (Int) -> IntOffset,
    content: @Composable () -> Unit,
) {
    val holder: GuiyInventoryHolder = LocalInventoryHolder.current

    val canvas = remember { InventoryCanvas() }

//    val existingInventory =/* runCatching { */LocalInventory.current /*}.getOrNull()*/

//    if (existingInventory != null) {
//        SideEffect {
//            guiyPlugin.injectedLogger().e {
//                "Creating inventory $inventory inside other inventory ($existingInventory), Guiy does not support this yet."
//            }
//        }
//        return
//    }

    CompositionLocalProvider(
        LocalInventory provides inventory
    ) {
        Box(
            // TODO Consume click so only the visible inventory can process clicks
            modifier = modifier.clickable { isConsumed = false }.drawWithContent(canvas) {
                canvas.clear()
                drawContent()

                // Copy canvas contents to inventory based on gridToInventoryIndex mapping
                val items = canvas.contents()
                repeat(inventory.size) { index ->
                    val coords = inventoryIndexToGrid(index)
                    if (items[coords] == null) inventory.setItem(index, null)
                }
                for ((coords, item) in items) {
                    val index = gridToInventoryIndex(coords) ?: continue
                    if (index !in 0..<inventory.size) continue
                    val invItem = inventory.getItem(index)
                    if (invItem != item) inventory.setItem(index, item)
                }

                // Open the inventory, the last to render wins
                holder.setActiveInventory(GuiyInventory(inventory, onClose, title))
            },
        ) { content() }
    }
}
