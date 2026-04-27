package com.mineinabyss.guiy.modifiers.click

import com.mineinabyss.guiy.interaction.ClickEvent
import com.mineinabyss.guiy.modifiers.InteractionModifierNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement
import org.bukkit.event.inventory.ClickType

data class ClickModifierElement(
    val onClick: (ClickEvent.() -> Unit),
) : ModifierNodeElement<ClickModifierNode>() {
    override fun create(): ClickModifierNode = ClickModifierNode(onClick)

    override fun update(node: ClickModifierNode) {
        node.onClick = onClick
    }

}

class ClickModifierNode(
    var onClick: (ClickEvent.() -> Unit),
): Modifier.Node(), InteractionModifierNode {
    override fun onEvent(event: ClickEvent) {
        onClick(event)
    }
}

/**
 * A click listener. Will ignore double clicks as Minecraft sends two individual click events
 * AND a double click. To handle these events manually, use [onClickEvent].
 */
fun Modifier.clickable(
    onClick: ClickEvent.() -> Unit,
) = onClickEvent {
    if (this.clickType != ClickType.DOUBLE_CLICK) {
        isConsumed = true
        onClick()
    }
}

/**
 * A click listener, passing through all inventory click events in this composable's bounds.
 *
 * NOTE: This passes a DOUBLE_CLICK event as well as two individual click events. Use [clickable] if this is not desired.
 */
fun Modifier.onClickEvent(
    onClick: ClickEvent.() -> Unit,
) = then(ClickModifierElement(onClick = onClick))
