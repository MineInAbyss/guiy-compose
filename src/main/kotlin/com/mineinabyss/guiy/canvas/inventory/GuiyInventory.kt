package com.mineinabyss.guiy.canvas.inventory

import androidx.compose.runtime.Immutable
import org.bukkit.inventory.Inventory

@Immutable
data class GuiyInventory(
    val inventory: Inventory,
    val onClose: InventoryCloseScope.() -> Unit,
)
