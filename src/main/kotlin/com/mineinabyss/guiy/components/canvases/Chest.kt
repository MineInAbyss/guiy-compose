package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.inventory.GuiyHolder
import com.mineinabyss.guiy.nodes.ChestInventory
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.nodes.InventoryNode
import com.mineinabyss.guiy.remember.rememberViewers
import org.bukkit.entity.Player

@Composable
fun Chest(
    viewers: List<Player>,
    title: String,
    height: Int = 6,
    onClose: ((player: Player) -> Unit)? = null,
    children: @Composable () -> Unit
) {
    ComposeNode<ChestInventory, GuiyNodeApplier>(
        factory = {
            println("$height $title")
            ChestInventory(height, title)
                  },
        update = {
            set(viewers.toList()) { this.viewers = it }
            set(height) { this.height = height }
            set(title) { this.title = title }
            set(onClose) { this.onClose = onClose }
        },
        content = children,
    )
}

