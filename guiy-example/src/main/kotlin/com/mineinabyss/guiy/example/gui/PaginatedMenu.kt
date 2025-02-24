package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.HorizontalGrid
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.lists.NavbarPosition
import com.mineinabyss.guiy.components.lists.Paginated
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.modifiers.fillMaxSize
import com.mineinabyss.guiy.modifiers.size
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Composable
fun PaginatedMenu() {
    Chest(
        "Pagination example",
        modifier = Modifier.fillMaxSize()
    ) {
        var items by remember {
            val materials = Material.entries
            mutableStateOf((1..100).map { ItemStack(materials[it]) })
        }
        var page by remember { mutableStateOf(0) }
        Paginated(
            items,
            page = page,
            navbarPosition = NavbarPosition.END,
            previousButton = { Item(Material.RED_CONCRETE, "Previous", modifier = Modifier.clickable { page-- }) },
            nextButton = { Item(Material.BLUE_CONCRETE, "Next", modifier = Modifier.clickable { page++ }) },
        ) { pageItems ->
            HorizontalGrid(Modifier.size(4, 5)) {
                pageItems.forEach { item ->
                    Item(item, modifier = Modifier.clickable { items = items - item })
                }
            }
        }
    }
}
