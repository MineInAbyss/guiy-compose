package com.mineinabyss.guiy.modifiers

import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

interface ClickScope {
    val clickType: ClickType
    val slot: Int
    val item: ItemStack?
}

open class ClickModifier(
    val merged: Boolean = false,
    val onClick: (ClickScope.() -> Unit),
) : Modifier.Element<ClickModifier> {
    override fun mergeWith(other: ClickModifier) = ClickModifier(merged = true) {
        if (!other.merged)
            onClick()
        other.onClick(this)
    }
}
fun Modifier.clickable(onClick: ClickScope.() -> Unit) = then(ClickModifier(onClick = onClick))
