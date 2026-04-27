package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.VerticalGrid
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.lists.NavbarPosition
import com.mineinabyss.guiy.components.lists.ScrollDirection
import com.mineinabyss.guiy.components.lists.Scrollable
import com.mineinabyss.guiy.components.lists.ScrollableState
import me.dvyy.compose.mini.layout.modifiers.fillMaxSize
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Composable
fun ScrollingMenu() {
    Chest(
        "Scrolling example",
        modifier = Modifier.fillMaxSize()
    ) {
        val items = remember {
            val materials = Material.entries
            (1..100).map { ItemStack(materials[it]) }
        }
        Scrollable(
            items,
            state = remember { ScrollableState(ScrollDirection.VERTICAL) },
            navbarPosition = NavbarPosition.END,
        ) { pageItems ->
            VerticalGrid(Modifier.fillMaxSize()) {
                pageItems.forEach { item ->
                    Item(item)
                }
            }
        }
    }
}
