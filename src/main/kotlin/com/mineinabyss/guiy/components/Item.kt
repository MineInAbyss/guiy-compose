package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.components.canvases.LocalInventory
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.MeasureResult
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.sizeIn
import org.bukkit.inventory.ItemStack

@Composable
fun Item(itemStack: ItemStack, modifier: Modifier = Modifier) {
    val canvas = LocalInventory.current
    Layout(
        measurePolicy = { measurables, constraints ->
            MeasureResult(constraints.minWidth, constraints.minHeight) {}
        },
        renderer = { node ->
            val inv = canvas
            for (x in 0 until node.width)
                for (y in 0 until node.height)
                    set(inv, x, y, itemStack)
        },
        modifier = Modifier.sizeIn(minWidth = 1, minHeight = 1).then(modifier)
    )
}
