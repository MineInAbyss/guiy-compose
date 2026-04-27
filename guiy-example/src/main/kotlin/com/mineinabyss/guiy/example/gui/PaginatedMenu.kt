package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.HorizontalGrid
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.button.Button
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.items.Text
import com.mineinabyss.guiy.components.lists.*
import com.mineinabyss.guiy.modifiers.click.clickable
import me.dvyy.compose.mini.layout.modifiers.fillMaxSize
import me.dvyy.compose.mini.modifier.Modifier
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
            mutableStateOf((1..103).map { ItemStack(materials[it]) })
        }
        val scrollState = rememberScrollableState(ScrollDirection.PAGINATED)
        Scrollable(
            items,
            state = scrollState,
            navbarPosition = NavbarPosition.BOTTOM,
            scrollbar = {
                HorizontalScrollbar(
                    scrollState,
                    prevButton = { Button(onClick = { scrollState.previousPage() }) { Text("Custom Previous") } },
                    nextButton = { Button(onClick = { scrollState.nextPage() }) { Text("Custom Next") } }
                )
            }
//            previousButton = { Item(Material.RED_CONCRETE, "Previous") },
//            nextButton = { Item(Material.BLUE_CONCRETE, "Next") },
        ) { pageItems ->
            HorizontalGrid(Modifier.fillMaxSize()) {
                pageItems.forEach { item ->
                    Item(item, modifier = Modifier.clickable { items = items - item })
                }
            }
        }
    }
}
