package com.mineinabyss.guiy.modifiers.click

import com.mineinabyss.guiy.modifiers.Modifier
import org.bukkit.event.inventory.ClickType

open class ClickModifier(
    val merged: Boolean = false,
    val consumeClick: Boolean,
    val onClick: (ClickScope.() -> Unit),
//    val allowClick: (ClickScope.() -> Boolean)
) : Modifier.Element<ClickModifier> {
    override fun mergeWith(other: ClickModifier) = ClickModifier(
        merged = true,
        consumeClick = consumeClick || other.consumeClick,
        onClick = {
            if (!other.merged)
                onClick()
            other.onClick(this)
        },
    )
}

/**
 * A click listener. Will ignore double clicks as Minecraft sends two individual click events
 * AND a double click. To handle these events manually, use [onClickEvent].
 */
fun Modifier.clickable(
    consumeClick: Boolean = false,
    onClick: ClickScope.() -> Unit,
) = onClickEvent(consumeClick) {
    if (this.clickType != ClickType.DOUBLE_CLICK) onClick()
}

/**
 * A click listener, passing through all inventory click events in this composable's bounds.
 *
 * NOTE: This passes a DOUBLE_CLICK event as well as two individual click events. Use [clickable] if this is not desired.
 */
fun Modifier.onClickEvent(
    consumeClick: Boolean = false,
    onClick: ClickScope.() -> Unit,
) = then(
    ClickModifier(
        consumeClick = consumeClick,
        onClick = onClick,
    )
)
