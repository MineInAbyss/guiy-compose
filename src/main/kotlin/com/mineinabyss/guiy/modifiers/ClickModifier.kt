package com.mineinabyss.guiy.modifiers

import org.bukkit.event.inventory.ClickType

interface ClickScope {
    val clickType: ClickType
}

class ClickModifier(val onClick: (ClickScope.() -> Unit)) : Modifier.Element<ClickModifier> {
    override fun mergeWith(other: ClickModifier) = ClickModifier {
        onClick()
        other.onClick(this)
    }
}

fun Modifier.clickable(onClick: ClickScope.() -> Unit) = then(ClickModifier(onClick))

