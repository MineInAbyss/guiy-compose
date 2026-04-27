package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Spacer
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.modifiers.click.clickable
import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.layout.jetpack.Row
import me.dvyy.compose.mini.layout.modifiers.fillMaxSize
import me.dvyy.compose.mini.layout.modifiers.height
import me.dvyy.compose.mini.layout.modifiers.offset
import me.dvyy.compose.mini.layout.modifiers.width
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Material

@Composable
fun Cursor() {
    Chest(
        "Moving cursor",
        modifier = Modifier.height(2.dp)
    ) {
        var offset by remember { mutableStateOf(IntOffset(0, 0)) }
        Item(Material.BLACK_CONCRETE, modifier = Modifier.offset(offset.x.dp, offset.y.dp))
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Row {
                Item(
                    Material.RED_CONCRETE,
                    "Move left",
                    modifier = Modifier.clickable { offset = IntOffset(offset.x - 1, offset.y) })
                Spacer(Modifier.width(1.dp))
                Item(
                    Material.BLUE_CONCRETE,
                    "Move right",
                    modifier = Modifier.clickable { offset = IntOffset(offset.x + 1, offset.y) })
            }
        }
    }
}
