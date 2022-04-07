package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.inventory.GuiyOwner
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.clickable
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Composable
fun GuiyOwner.MainMenu(player: Player) {
    Chest(
        setOf(player),
        "Hello world",
        onClose = { exit() },
        modifier = Modifier.clickable { println("Chest clicked") }
    ) {
        var item: ItemStack? by remember { mutableStateOf(ItemStack(Material.STONE)) }
        Item(item, Modifier.clickable {
            val temp = cursor
            cursor = item
            item = temp
        })
    }
}
