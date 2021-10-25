package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.BoxNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.nodes.ItemNode
import org.bukkit.inventory.ItemStack

@Composable
fun Item(itemStack: ItemStack, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    ComposeNode<ItemNode, GuiyNodeApplier>(
        factory = { ItemNode() },
        update = {
            set(modifier) { this.modifier = modifier }
            set(itemStack) { this.item = itemStack }
            set(onClick) { this.onClick = onClick }
        }
    )
}
