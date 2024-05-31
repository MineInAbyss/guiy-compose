package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.fillMaxHeight
import kotlinx.coroutines.delay
import org.bukkit.entity.Player

@Composable
fun AnimatedTitle(player: Player) {
    val owner = LocalGuiyOwner.current
    var seconds by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds++
        }
    }
    Chest(
        setOf(player),
        "<red>${seconds}s have passed!",
        onClose = { owner.exit() },
        modifier = Modifier.fillMaxHeight()
    ) {
    }
}
