package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Spacer
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.state.IntOffset
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
import com.mineinabyss.guiy.jetpack.Alignment
import com.mineinabyss.guiy.layout.Box
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.modifiers.fillMaxSize
import com.mineinabyss.guiy.modifiers.height
import com.mineinabyss.guiy.modifiers.placement.absolute.at
import com.mineinabyss.guiy.modifiers.width
import org.bukkit.Material
import org.bukkit.entity.Player

@Composable
fun Cursor(player: Player) {
    val owner = LocalGuiyOwner.current
    Chest(
        setOf(player),
        "Moving cursor",
        onClose = { owner.exit() },
        modifier = Modifier.height(2)
    ) {
        var offset by remember { mutableStateOf(IntOffset(0, 0)) }

        Item(Material.BLACK_CONCRETE, modifier = Modifier.at(offset.x, offset.y))
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Row {
                Item(
                    Material.RED_CONCRETE,
                    "Move left",
                    modifier = Modifier.clickable { offset = IntOffset(offset.x - 1, offset.y) })
                Spacer(Modifier.width(1))
                Item(
                    Material.BLUE_CONCRETE,
                    "Move right",
                    modifier = Modifier.clickable { offset = IntOffset(offset.x + 1, offset.y) })
            }
        }
    }
}
