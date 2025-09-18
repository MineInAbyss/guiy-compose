package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.state.IntCoordinates
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.canvas.*
import com.mineinabyss.guiy.canvas.GuiyCanvas
import com.mineinabyss.guiy.canvas.inventory.GuiyInventory
import com.mineinabyss.guiy.canvas.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.canvas.inventory.InventoryCloseScope
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.Renderer
import com.mineinabyss.guiy.layout.StaticMeasurePolicy
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.nodes.GuiyNode
import com.mineinabyss.idofront.messaging.injectedLogger
import com.mineinabyss.idofront.nms.entities.title
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
    gridToInventoryIndex: (IntCoordinates) -> Int?,
    inventoryIndexToGrid: (Int) -> IntCoordinates,
    content: @Composable () -> Unit,
) {
    val holder: GuiyInventoryHolder = LocalInventoryHolder.current

    // Update title
    LaunchedEffect(title) {
        if (title != null) inventory.viewers.forEach { it.openInventory.title(title) }
    }

    val canvas = remember { InventoryCanvas() }

    val existingInventory = runCatching { LocalInventory.current }.getOrNull()

    if (existingInventory != null) {
        SideEffect {
            guiyPlugin.injectedLogger().e {
                "Creating inventory $inventory inside other inventory ($existingInventory), Guiy does not support this yet."
            }
        }
        return
    }

    CompositionLocalProvider(
        LocalCanvas provides canvas,
        LocalInventory provides inventory
    ) {
        Layout(
            measurePolicy = StaticMeasurePolicy,
            renderer = object : Renderer {
                override fun GuiyCanvas.render(node: GuiyNode) {
                    // The last inventory to render sets this state so holder can choose which inventory to open
                    holder.setActiveInventory(GuiyInventory(inventory, onClose))
                    canvas.clear()
                }

                override fun GuiyCanvas.renderAfterChildren(node: GuiyNode) {
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
                }
            },
            // Consume click so only the visible inventory can process clicks
            modifier = modifier.clickable(consumeClick = true) { },
            content = content,
        )
    }
}
