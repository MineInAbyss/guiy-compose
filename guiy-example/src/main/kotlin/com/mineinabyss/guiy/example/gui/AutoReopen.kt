package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.canvas.LocalGuiyOwner
import com.mineinabyss.guiy.components.button.Button
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.items.Text
import me.dvyy.compose.mini.layout.modifiers.fillMaxHeight
import me.dvyy.compose.mini.modifier.Modifier

/**
 * Demonstrates how to prevent players from closing a GUI on `Esc`.
 */
@Composable
fun AutoReopen() {
    Chest(
        title = "This menu will reopen on `Esc`",
        modifier = Modifier.fillMaxHeight(),
        onClose = { }
    ) {
        val owner = LocalGuiyOwner.current
        Button(onClick = { owner.exit() }) { Text("Close") }
    }
}
