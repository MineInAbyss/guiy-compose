package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.inventory.GuiyOwner
import com.mineinabyss.guiy.nodes.ChestCanvas
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.nodes.InventoryCanvasScope
import com.mineinabyss.guiy.nodes.InventoryCloseScope
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
            set(height) { this.height = it }
            set(title) { this.title = it }
            set(onClose) { this.onClose = it }
            set(this@Chest) { owner = it }
            init { this@Chest.canvas = this }
        },
        content = { InventoryCanvasScope.children() },
    )
}
