package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Spacer
import com.mineinabyss.guiy.layout.Box
import com.mineinabyss.guiy.layout.Column
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.layout.alignment.Alignment
import com.mineinabyss.guiy.modifiers.*
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.messaging.broadcast
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Composable
fun <T> Paginated(
    items: List<T>,
    page: Int,
    itemsPerPage: Int,
    nextButton: @Composable () -> Unit,
    previousButton: @Composable () -> Unit,
    navbarPosition: NavbarPosition = NavbarPosition.BOTTOM,
    navbarFiller: ItemStack? = ItemStack(Material.GRAY_STAINED_GLASS_PANE).editItemMeta { isHideTooltip = true },
    content: @Composable (page: List<T>) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        val start = page * itemsPerPage
        val end = (page + 1) * itemsPerPage

        val alignment = when (navbarPosition) {
            NavbarPosition.START -> Alignment.CenterStart
            NavbarPosition.END -> Alignment.CenterEnd
            NavbarPosition.TOP -> Alignment.TopCenter
            NavbarPosition.BOTTOM -> Alignment.BottomCenter
        }

        val pageItems = remember(start, end) {
            items.subList(start, end.coerceAtMost(items.size))
        }
        content(pageItems)

        Box(contentAlignment = alignment, modifier = Modifier.fillMaxSize()) {

            when (navbarPosition) {
                NavbarPosition.START, NavbarPosition.END -> {
                    Item(navbarFiller, modifier = Modifier.fillMaxHeight())
                    if (page > 0) previousButton()
                    else Spacer(height = 1)
                    Spacer(height = 1)
                    if (end < items.size) nextButton()
                    else Spacer(height = 1)
                }

                NavbarPosition.TOP, NavbarPosition.BOTTOM -> {
                    Item(navbarFiller, modifier = Modifier.fillMaxWidth())
                    if (page > 0) previousButton()
                    else Spacer(width = 1)
                    Spacer(width = 1)
                    if (end < items.size) nextButton()
                    else Spacer(width = 1)
                }
            }
        }
    }
}

enum class NavbarPosition {
    START, END, TOP, BOTTOM
}
