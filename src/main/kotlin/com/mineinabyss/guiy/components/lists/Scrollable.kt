package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Spacer
import com.mineinabyss.guiy.layout.Box
import com.mineinabyss.guiy.layout.Size
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.fillMaxSize
import com.mineinabyss.guiy.modifiers.onSizeChanged
import com.mineinabyss.idofront.items.editItemMeta
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class ScrollDirection {
    VERTICAL, HORIZONTAL;
}

/**
 * A scrollable list of items, with buttons to go to the next and previous lines.
 *
 * Content must set a size or fillMaxSize Modifier to be visible.
 */
@Composable
fun <T> Scrollable(
    items: List<T>,
    line: Int,
    scrollDirection: ScrollDirection,
    nextButton: @Composable () -> Unit,
    previousButton: @Composable () -> Unit,
    navbarPosition: NavbarPosition = NavbarPosition.BOTTOM,
    navbarBackground: ItemStack? = remember {
        ItemStack(Material.GRAY_STAINED_GLASS_PANE).editItemMeta {
            isHideTooltip = true
        }
    },
    content: @Composable (page: List<T>) -> Unit,
) {
    var size by remember { mutableStateOf(Size(0, 0)) }
    val itemsPerLine = if (scrollDirection == ScrollDirection.VERTICAL) size.width else size.height
    val totalLines = if (scrollDirection == ScrollDirection.VERTICAL) size.height else size.width
    Box(Modifier.fillMaxSize()) {
        val start = line * itemsPerLine
        val end = start + (itemsPerLine * totalLines)
        val pageItems = remember(start, end) {
            if (start < 0) emptyList()
            else items.subList(start, end.coerceAtMost(items.size))
        }
        NavbarLayout(
            position = navbarPosition,
            navbar = {
                NavbarButtons(navbarPosition, navbarBackground) {
                    if (line > 0) previousButton()
                    else Spacer(1, 1)
                    if (end < items.size) nextButton()
                    else Spacer(1, 1)
                }
            },
            content = {
                Box(Modifier.onSizeChanged {
                    size = it
                }) {
                    content(pageItems)
                }
            }
        )
    }
}
