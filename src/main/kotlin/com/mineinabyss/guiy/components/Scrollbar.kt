package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import org.bukkit.inventory.ItemStack

@Composable
fun Grid(width: Int, maxHeight: Int, content: @Composable () -> Unit) {
    content()
}

interface LazyGridScope {
    fun item(content: @Composable () -> Unit)
}
