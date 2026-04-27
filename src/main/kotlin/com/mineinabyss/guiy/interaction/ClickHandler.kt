package com.mineinabyss.guiy.interaction

import com.mineinabyss.guiy.components.ClickResult

interface ClickHandler {
    fun processClick(scope: ClickEvent): ClickResult
}