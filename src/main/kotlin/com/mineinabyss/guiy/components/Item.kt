package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.layout.ChildPlacer
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.modifiers.Modifier
import org.bukkit.inventory.ItemStack

@Composable
fun Item(itemStack: ItemStack, modifier: Modifier = Modifier) {
    Layout(
        measurer = { MeasureResult(1, 1) },
        placer = ChildPlacer,
        renderer = { set(0, 0, itemStack) },
        modifier = modifier
    )
//    ComposeNode<ItemNode, GuiyNodeApplier>(
//        factory = { ItemNode() },
//        update = {
//            set(modifier) { this.modifier = modifier }
//            set(itemStack) { this.item = itemStack }
//        }
//    )
}
