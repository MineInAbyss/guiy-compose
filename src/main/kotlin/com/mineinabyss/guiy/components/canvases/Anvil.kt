package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mineinabyss.guiy.canvas.CurrentPlayer
import com.mineinabyss.guiy.canvas.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.canvas.inventory.InventoryCloseScope
import com.mineinabyss.guiy.components.items.InvisibleItem
import com.mineinabyss.guiy.components.rememberMiniMsg
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.idofront.textcomponents.toPlainText
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.layout.jetpack.Row
import me.dvyy.compose.mini.layout.modifiers.size
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

@Composable
fun Anvil(
    title: String,
    onTextChanged: (String) -> Unit,
    onClose: InventoryCloseScope.() -> Unit = { back() },
    onSubmit: (String) -> Unit = {},
    inputLeft: @Composable () -> Unit = { InvisibleItem() },
    inputRight: @Composable () -> Unit = { InvisibleItem() },
    output: @Composable () -> Unit = {},
) {
    val holder: GuiyInventoryHolder = LocalInventoryHolder.current
    val player = CurrentPlayer
    val titleMM = rememberMiniMsg(title)
    val inventory: Inventory = remember(holder) {
        Bukkit.getServer().createInventory(holder, InventoryType.ANVIL, titleMM)
    }

    // Track updates to anvil text via packets
    var playerViewText by remember(inventory) { mutableStateOf("") }
    LaunchedEffect(player) {
        guiyPlugin.anvilPacketFlow.filter { it?.second == player }.filterNotNull().collect {
            val text = it.first.ifEmpty { inventory.getItem(0)?.effectiveName()?.toPlainText() ?: "" }
            playerViewText = text
            onTextChanged(text)
        }
    }

    val constrainedModifier = Modifier.size(width = 3.dp, height = 1.dp)

    Inventory(
        inventory,
        onClose = onClose,
        title = titleMM,
        modifier = constrainedModifier,
        gridToInventoryIndex = { it.x },
        inventoryIndexToGrid = { IntOffset(it, 0) }) {
        Row {
            Box(Modifier.size(1.dp)) {
                inputLeft()
            }
            Box(Modifier.size(1.dp)) {
                inputRight()
            }
            Box(Modifier.size(1.dp).clickable { onSubmit(playerViewText) }) {
                output()
            }
        }
    }
}
