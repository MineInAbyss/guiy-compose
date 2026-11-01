package com.mineinabyss.guiy.modifiers

import me.dvyy.compose.mini.modifier.DelegatableNode
import org.bukkit.inventory.ItemStack

/** Rewrites every item drawn inside this node's bounds, inner nodes are applied first */
interface ItemTransformModifierNode : DelegatableNode {
    fun transform(itemStack: ItemStack): ItemStack
}
