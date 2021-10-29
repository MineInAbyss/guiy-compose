package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import com.mineinabyss.guiy.inventory.GuiyOwner
import com.mineinabyss.guiy.nodes.*
import org.bukkit.entity.Player

@Composable
fun GuiyOwner.Chest(
    viewers: List<Player>,
    title: String,
    height: Int = 6,
    onClose: (InventoryCloseScope.(player: Player) -> Unit)? = null,
    children: @Composable InventoryCanvasScope.() -> Unit
) {
    ComposeNode<ChestCanvas, GuiyNodeApplier>(
        factory = { ChestCanvas(height, title) },
        update = {
            set(viewers.toList()) { this.viewers = it }
            set(height) { this.height = height }
            set(title) { this.title = title }
            set(onClose) { this.onClose = onClose }
            init { this@Chest.canvas = this }
        },
        content = { InventoryCanvasScope.children() },
    )
}