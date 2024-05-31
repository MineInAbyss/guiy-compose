package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.inventory.LocalCanvas
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.Size
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.onSizeChanged
import com.mineinabyss.guiy.modifiers.sizeIn
import com.mineinabyss.guiy.nodes.InventoryCloseScope
import com.mineinabyss.guiy.nodes.StaticMeasurePolicy
import com.mineinabyss.idofront.entities.title
import com.mineinabyss.idofront.textcomponents.miniMsg
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

const val CHEST_WIDTH = 9
const val MIN_CHEST_HEIGHT = 1
const val MAX_CHEST_HEIGHT = 6

/**
 * A Chest GUI [Inventory] composable overload.
 *
 * @param title The title of the Chest inventory, formatted with MiniMessage.
 */
@Composable
fun Chest(
    viewers: Set<Player>,
    title: String,
    modifier: Modifier = Modifier,
    onClose: (InventoryCloseScope.(player: Player) -> Unit) = {},
    content: @Composable () -> Unit,
) {
    Chest(viewers, title.miniMsg(), modifier, onClose, content)
}

/**
 * A Chest GUI [Inventory] composable.
 *
 * @param viewers The set of players who will view the inventory.
 * @param title The title of the Chest inventory.
 * @param modifier The modifier for the Chest GUI, default is Modifier.
 * @param onClose The function to be executed when the Chest GUI is closed, default is an empty function.
 * @param content The content of the Chest GUI, defined as a Composable function.
 */
@Composable
fun Chest(
    viewers: Set<Player>,
    title: Component,
    modifier: Modifier = Modifier,
    onClose: (InventoryCloseScope.(player: Player) -> Unit) = {},
    content: @Composable () -> Unit,
) {
    var size by remember { mutableStateOf(Size()) }
    val constrainedModifier = modifier.sizeIn(CHEST_WIDTH, CHEST_WIDTH, MIN_CHEST_HEIGHT, MAX_CHEST_HEIGHT)
        .onSizeChanged { if (size != it) size = it }

    val canvas = remember(size) {
        object : GuiyCanvas {
            override fun set(inventory: Inventory, x: Int, y: Int, item: ItemStack?) {
                if (/*!updating && */x in 0 until size.width && y in 0 until size.height) {
                    val slot = y * size.width + x
                    if (slot < inventory.size)
                        inventory.setItem(slot, item)
                }
            }
        }
    }

    val holder = rememberInventoryHolder(viewers, onClose)

    // Create new inventory when any appropriate value changes
    val inventory: Inventory = remember(size) {
        if (size == Size()) return@remember null
        Bukkit.createInventory(holder, CHEST_WIDTH * size.height, title).also {
            holder.activeInventory = it
        }
    } ?: run {
        Layout(
            measurePolicy = StaticMeasurePolicy,
            modifier = constrainedModifier
        )
        return
    }

    LaunchedEffect(title) {
        // This just sends a packet, doesn't need to be on sync thread
        inventory.viewers.forEach { it.openInventory.title(title) }
    }

    //TODO handle sending correct title when player list changes

    CompositionLocalProvider(LocalCanvas provides canvas) {
        Inventory(
            inventory = inventory,
            viewers = viewers,
            modifier = constrainedModifier
        ) {
            content()
        }
    }
}
