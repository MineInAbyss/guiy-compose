package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.fillMaxHeight
import kotlinx.coroutines.delay

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
