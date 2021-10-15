package com.mineinabyss.guiy

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mineinabyss.guiy.components.Box
import kotlinx.coroutines.delay
import runGuiy
import kotlin.coroutines.coroutineContext

fun main() {
    runGuiy {
        println(coroutineContext)
        var text by mutableStateOf(10)
        setContent {
            Box(text, 1) {
                println("Reran box $text")
            }
            LaunchedEffect(Unit) {
                for (i in 1..20) {
                    delay(100)
                    text = i
                }
            }
        }

    }
}
