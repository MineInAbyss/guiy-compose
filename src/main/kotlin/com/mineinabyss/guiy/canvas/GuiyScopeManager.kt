package com.mineinabyss.guiy.canvas

import kotlinx.coroutines.CoroutineScope

object GuiyScopeManager {
    val scopes = mutableSetOf<CoroutineScope>()
}
