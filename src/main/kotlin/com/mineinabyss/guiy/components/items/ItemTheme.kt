package com.mineinabyss.guiy.components.items

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.mineinabyss.idofront.items.editItemMeta
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Immutable
class ItemTheme(
    val invisible: ItemStack = ItemStack.of(Material.PAPER).editItemMeta { setCustomModelData(1) },
    val hiddenTooltip: ItemStack = invisible.clone().editItemMeta { isHideTooltip = true },
)

internal val DefaultItemTheme = ItemTheme()

val LocalItemTheme = staticCompositionLocalOf<ItemTheme> { DefaultItemTheme }
