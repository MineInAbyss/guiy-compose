package com.mineinabyss.guiy.example.gui

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.VerticalGrid
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.components.lists.NavbarPosition
import com.mineinabyss.guiy.components.lists.Paginated
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.modifiers.fillMaxSize
import com.mineinabyss.guiy.modifiers.placement.absolute.at
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Composable
fun PaginatedMenu(player: Player) {
    val owner = LocalGuiyOwner.current
    Chest(
        setOf(player),
        "Pagination example",
        onClose = { owner.exit() },
        modifier = Modifier.fillMaxSize()
    ) {
        val items = remember {
            val materials = Material.entries
            (1..100).map { ItemStack(materials[it]) }
        }
        var page by remember { mutableStateOf(0) }
        Paginated(
            items,
            page = page,
            itemsPerPage = 9 * 5,
            previousButton = { Item(Material.RED_CONCRETE, "Previous", modifier = Modifier.at(0, 1).clickable { page-- }) },
            navbarPosition = NavbarPosition.START,
            nextButton = { Item(Material.BLUE_CONCRETE, "Next", modifier = Modifier.at(0,3).clickable { page++ }) },
        ) { pageItems ->
            VerticalGrid {
                pageItems.forEach { item ->
                    Item(item)
                }
            }
        }
    }
}
