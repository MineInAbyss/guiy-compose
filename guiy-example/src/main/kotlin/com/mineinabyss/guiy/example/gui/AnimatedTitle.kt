package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.canvases.Chest
import kotlinx.coroutines.delay
import me.dvyy.compose.mini.layout.modifiers.fillMaxHeight
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun AnimatedTitle() {
    var seconds by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds++
        }
    }
    Chest(
        title = "<red>${seconds}s have passed!",
        modifier = Modifier.fillMaxHeight()
    ) {
    }
}
