package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.rememberMiniMsg
import com.mineinabyss.guiy.components.state.IntCoordinates
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
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
    title: String,
    modifier: Modifier = Modifier,
    onClose: (InventoryCloseScope.(player: Player) -> Unit) = { exit() },
    content: @Composable () -> Unit,
) {
    val titleMM = rememberMiniMsg(title)
    Chest(titleMM, modifier, onClose, content)
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
    title: Component,
    modifier: Modifier = Modifier,
    onClose: (InventoryCloseScope.(player: Player) -> Unit) = {},
    content: @Composable () -> Unit,
) {
    var size by remember { mutableStateOf(Size()) }
    val constrainedModifier =
        Modifier.sizeIn(CHEST_WIDTH, CHEST_WIDTH, MIN_CHEST_HEIGHT, MAX_CHEST_HEIGHT).then(modifier)
        .onSizeChanged { if (size != it) size = it }

    val owner = LocalGuiyOwner.current
    val viewers by owner.viewers.collectAsState()
    val holder = rememberInventoryHolder(viewers, onClose)

    // Create new inventory when any appropriate value changes

    // Draw nothing if empty
    if (size == Size()) {
        Layout(
            measurePolicy = StaticMeasurePolicy,
            modifier = constrainedModifier
        )
        return
    }

    val inventory: Inventory = remember(size) {
        Bukkit.createInventory(holder, CHEST_WIDTH * size.height, title).also {
            holder.setActiveInventory(it)
        }
    }

    LaunchedEffect(title) {
        // This just sends a packet, doesn't need to be on sync thread
        inventory.viewers.forEach { it.openInventory.title(title) }
    }

    //TODO handle sending correct title when player list changes
    Inventory(
        inventory = inventory,
        viewers = viewers,
        modifier = constrainedModifier,
        gridToInventoryIndex = { (x, y) ->
            if (x !in 0 until CHEST_WIDTH || y !in 0 until size.height) null
            else x + y * CHEST_WIDTH
        },
        inventoryIndexToGrid = { index ->
            IntCoordinates(index % CHEST_WIDTH, index / CHEST_WIDTH)
        },
    ) {
        content()
    }
}
