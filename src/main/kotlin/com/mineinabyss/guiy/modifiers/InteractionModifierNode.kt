package com.mineinabyss.guiy.modifiers

import com.mineinabyss.guiy.interaction.ClickEvent
import me.dvyy.compose.mini.modifier.DelegatableNode

interface InteractionModifierNode : DelegatableNode {
    fun onEvent(event: ClickEvent)
}