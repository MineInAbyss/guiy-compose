package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.ItemGrid
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.rememberItemGridState
import com.mineinabyss.guiy.inventory.GuiyOwner
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.clickable
import com.mineinabyss.guiy.modifiers.size
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Composable
fun GuiyOwner.MainMenu(player: Player) {
    val title = "Hello world"
    Chest(
        setOf(player),
        title,
        onClose = { exit() },
        modifier = Modifier.clickable { println("Chest clicked") }
    ) {
        val state = rememberItemGridState()
        ItemGrid(state, Modifier.size(4, 1))
    }
}
