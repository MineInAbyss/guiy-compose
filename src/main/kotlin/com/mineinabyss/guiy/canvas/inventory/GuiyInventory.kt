package com.mineinabyss.guiy.canvas.inventory

import androidx.compose.runtime.Immutable
import net.kyori.adventure.text.Component
import org.bukkit.inventory.Inventory

@Immutable
data class GuiyInventory(
    val inventory: Inventory,
    val onClose: InventoryCloseScope.() -> Unit,
    val title: Component?,
)
