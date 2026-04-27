package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.VerticalGrid
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.lists.NavbarPosition
import com.mineinabyss.guiy.components.lists.ScrollDirection
import com.mineinabyss.guiy.components.lists.Scrollable
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
        var line by remember { mutableStateOf(0) }
        Scrollable(
            items,
            line = line,
            onLineChange = { line = it },
            scrollDirection = ScrollDirection.VERTICAL,
            navbarPosition = NavbarPosition.END,
            previousButton = { Item(Material.RED_CONCRETE, "Previous") },
            nextButton = { Item(Material.BLUE_CONCRETE, "Next") },
        ) { pageItems ->
            VerticalGrid(Modifier.fillMaxSize()) {
                pageItems.forEach { item ->
                    Item(item)
                }
            }
        }
    }
}
