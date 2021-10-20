package com.mineinabyss.guiy.inventory

import kotlinx.coroutines.CoroutineScope

object GuiyScopeManager {
    val scopes = mutableSetOf<CoroutineScope>()
}
