package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.Stable
import org.bukkit.entity.Player

@Stable
interface InventoryCloseScope {
    val player: Player
    fun exit()
    fun back()
}
