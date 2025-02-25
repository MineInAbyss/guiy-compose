package com.mineinabyss.guiy.components.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.rememberMiniMsg
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.idofront.items.editItemMeta
import net.kyori.adventure.text.Component

/**
 * Text with invisible item, use [LocalItemTheme] to choose the invisible item based on your resourcepack.
 */
@Composable
fun Text(name: String, vararg lore: String, modifier: Modifier = Modifier.Companion) {
    val invisible = LocalItemTheme.current.invisible
    val mmName = rememberMiniMsg(name)
    val mmLore = rememberMiniMsg(*lore)
    val item = remember(name, lore, invisible) {
        invisible.clone().editItemMeta {
            itemName(mmName)
            lore(mmLore)
        }
    }
    Item(item, modifier)
}

/**
 * Text with invisible item, use [LocalItemTheme] to choose the invisible item based on your resourcepack.
 */
@Composable
fun Text(name: Component, vararg lore: Component, modifier: Modifier = Modifier) {
    val invisible = LocalItemTheme.current.invisible
    val item = remember(name, lore, invisible) {
        invisible.clone().editItemMeta {
            itemName(name)
            lore(lore.toList())
        }
    }
    Item(item, modifier)
}
