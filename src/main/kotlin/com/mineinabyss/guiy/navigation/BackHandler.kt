package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf

class BackGestureDispatcher {
    val listeners = mutableListOf<() -> Unit>()
    val activeListener get() = listeners.lastOrNull()

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    fun onBack() {
        activeListener?.invoke()
    }

    fun hasActiveListeners() = listeners.isNotEmpty()
}

val LocalBackGestureDispatcher = staticCompositionLocalOf<BackGestureDispatcher> { error("No Back Dispatcher provided.") }

/**
 * A listener to back events (usually a menu being closed with `esc`).
 */
@Composable
fun BackHandler(onBack: () -> Unit) {
    val dispatcher = LocalBackGestureDispatcher.current
    DisposableEffect(dispatcher) {
        dispatcher.addListener(onBack)
        onDispose {
            dispatcher.removeListener(onBack)
        }
    }
}
