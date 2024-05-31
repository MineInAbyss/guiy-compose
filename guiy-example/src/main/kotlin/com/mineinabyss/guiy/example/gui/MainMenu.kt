package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.components.CreativeItem
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.fillMaxWidth
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Composable
fun MainMenu(player: Player) {
    val owner = LocalGuiyOwner.current
    val title = "Hello world"
    Chest(
        setOf(player),
        title,
        onClose = { owner.exit() },
    ) {
        Row {
            listOf(Material.DIAMOND, Material.EMERALD, Material.GOLD_INGOT, Material.IRON_INGOT)
                .forEach {
                    CreativeItem(ItemStack(it))
                }
            Item(Material.BARRIER, "<red>No interaction here", modifier = Modifier.fillMaxWidth())
        }
    }
}
