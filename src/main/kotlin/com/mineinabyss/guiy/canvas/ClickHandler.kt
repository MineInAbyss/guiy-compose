package com.mineinabyss.guiy.canvas

import com.mineinabyss.guiy.modifiers.click.ClickScope

interface ClickHandler {
    fun processClick(scope: ClickScope): ClickResult
}