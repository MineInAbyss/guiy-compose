package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.button.Button
import com.mineinabyss.guiy.components.canvases.Anvil
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.items.Text

@Composable
fun AnvilGUI() {
    var text by remember { mutableStateOf("") }
    var isAnvil by remember { mutableStateOf(false) }
    if (!isAnvil) Chest("Menu") {
        Button(onClick = { isAnvil = true }) {
            Text("Change text", "Current: $text")
        }
    }
    else {
        var input by remember { mutableStateOf("") }
        fun validate(input: String) = !input.contains("!")

        Anvil(
            title = "Input text",
            inputLeft = { Text(text) },
            onClose = { isAnvil = false },
            onTextChanged = { input = it },
            onSubmit = {
                if (validate(input)) {
                    text = input
                    isAnvil = false
                }
            },
            output = {
                if (validate(input)) Text("Submit '$input'?")
                else Text("<red>'$input' cannot contain ! signs.")
            },
        )
    }
}
