package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mineinabyss.guiy.canvas.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.canvas.inventory.InventoryCloseScope
import com.mineinabyss.guiy.components.rememberMiniMsg
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.layout.modifiers.onSizeChanged
import me.dvyy.compose.mini.layout.modifiers.sizeIn
import me.dvyy.compose.mini.modifier.Modifier
import net.kyori.adventure.text.Component
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
    onClose: InventoryCloseScope.() -> Unit = { back() },
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
    onClose: InventoryCloseScope.() -> Unit = { back() },
    content: @Composable () -> Unit,
) {
    val holder: GuiyInventoryHolder = LocalInventoryHolder.current
    var size by remember { mutableStateOf(IntSize(0, 0)) }
    val constrainedModifier =
        Modifier
            .onSizeChanged { if (size != it) size = it }
            .sizeIn(CHEST_WIDTH.dp, CHEST_WIDTH.dp, MIN_CHEST_HEIGHT.dp, MAX_CHEST_HEIGHT.dp)
            .then(modifier)

    // Draw nothing if empty
    if (size == IntSize(0, 0)) {
        Box(modifier= constrainedModifier) {  }
        return
    }

    val inventory: Inventory = remember(size) {
        holder.getOrCreateInventory(size.height, title)
    }

    Inventory(
        inventory = inventory,
        onClose = onClose,
        title = title,
        modifier = constrainedModifier,
        gridToInventoryIndex = { (x, y) ->
            if (x !in 0 until CHEST_WIDTH || y !in 0 until size.height) null
            else x + y * CHEST_WIDTH
        },
        inventoryIndexToGrid = { index ->
            IntOffset(index % CHEST_WIDTH, index / CHEST_WIDTH)
        },
    ) {
        content()
    }
}

