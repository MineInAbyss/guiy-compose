package com.mineinabyss.guiy.modifiers

import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.DragType
import org.bukkit.inventory.ItemStack

data class ClickScope(
    val clickType: ClickType,
    val slot: Int,
    var cursor: ItemStack?
)

data class DragScope(
    val dragType: DragType,
    val updatedItems: Map<Int, ItemStack>,
    var cursor: ItemStack?
)

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

open class DragModifier(
    val merged: Boolean = false,
    val onDrag: (DragScope.() -> Unit),
) : Modifier.Element<DragModifier> {
    override fun mergeWith(other: DragModifier) = DragModifier(merged = true) {
        if (!other.merged)
            onDrag()
        other.onDrag(this)
    }
}

fun Modifier.clickable(onClick: ClickScope.() -> Unit) = then(ClickModifier(onClick = onClick))
fun Modifier.draggable(onDrag: DragScope.() -> Unit) = then(DragModifier(onDrag = onDrag))
