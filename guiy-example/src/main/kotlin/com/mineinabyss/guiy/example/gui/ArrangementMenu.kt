package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
import com.mineinabyss.guiy.jetpack.Arrangement
import com.mineinabyss.guiy.layout.Column
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.fillMaxSize
import com.mineinabyss.guiy.modifiers.fillMaxWidth
import org.bukkit.Material
import org.bukkit.entity.Player

@Composable
fun ArrangementMenu(player: Player) {
    val owner = LocalGuiyOwner.current
    Chest(
        setOf(player),
        "Arrangement example",
        onClose = { owner.exit() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            val modifier = Modifier.fillMaxWidth()
            Row(modifier, horizontalArrangement = Arrangement.spacedBy(1)) {
                Items(4)
            }
            Row(modifier, horizontalArrangement = Arrangement.Center) {
                Items(3)
            }
            Row(modifier, horizontalArrangement = Arrangement.SpaceAround) {
                Items(3)
            }
            Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Items(3)
            }
//            Item(Material.BLACK_STAINED_GLASS, modifier = Modifier.fillMaxHeight())
//            Column {
//                Item(Material.RED_CONCRETE, modifier = Modifier.fillMaxWidth().height(3).padding(1))
//                Item(Material.BLUE_CONCRETE, modifier = Modifier.fillMaxSize().padding(1))
//            }
        }
    }
}

@Composable
private fun Items(count: Int) {
    repeat(count) {
        Item(Material.RED_CONCRETE)
    }
}
