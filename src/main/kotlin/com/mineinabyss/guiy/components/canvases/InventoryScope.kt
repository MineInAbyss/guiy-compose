package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import com.mineinabyss.guiy.inventory.GuiyInventoryHolder
import org.bukkit.inventory.Inventory

@Immutable
class InventoryScope(
    val holder: GuiyInventoryHolder
) {
    @Composable
    inline fun provideInventory(inventory: Inventory, crossinline content: @Composable () -> Unit) {
        holder.activeInventory = inventory
        CompositionLocalProvider(LocalInventory provides inventory) {
            content()
        }
    }
}
