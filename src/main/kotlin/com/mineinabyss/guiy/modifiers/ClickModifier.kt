package com.mineinabyss.guiy.modifiers

import org.bukkit.event.inventory.ClickType

interface ClickScope {
    val clickType: ClickType
}

class ClickModifier(val onClick: (ClickScope.() -> Unit)): Modifier.Element

fun Modifier.clickable(onClick: ClickScope.() -> Unit) = then(ClickModifier(onClick))

fun Modifier.getClickModifiers() = foldIn(mutableListOf<ClickModifier>()) { list, modifier ->
    if(modifier is ClickModifier)
        list.add(modifier)
    list
}
