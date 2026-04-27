package com.mineinabyss.guiy.modifiers

import com.mineinabyss.guiy.draw.ContentDrawScope
import com.mineinabyss.guiy.draw.InventoryCanvas
import me.dvyy.compose.mini.modifier.DelegatableNode

interface DrawModifierNode : DelegatableNode {
    val overrideCanvas: InventoryCanvas? get() = null
    fun ContentDrawScope.draw()
}