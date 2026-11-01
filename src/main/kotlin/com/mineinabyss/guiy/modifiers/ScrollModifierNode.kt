package com.mineinabyss.guiy.modifiers

import com.mineinabyss.guiy.interaction.ScrollEvent
import me.dvyy.compose.mini.modifier.DelegatableNode

interface ScrollModifierNode : DelegatableNode {
    fun onScroll(event: ScrollEvent)
}
