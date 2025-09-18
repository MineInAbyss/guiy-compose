package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Spacer
import com.mineinabyss.guiy.jetpack.Alignment
import com.mineinabyss.guiy.jetpack.Arrangement
import com.mineinabyss.guiy.layout.Box
import com.mineinabyss.guiy.layout.Column
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.layout.Size
import com.mineinabyss.guiy.modifiers.*
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.modifiers.placement.padding.padding
import com.mineinabyss.idofront.items.editItemMeta
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * A paginated list of items, with buttons to go to the next and previous pages.
 *
 * Content must set a size or fillMaxSize Modifier to be visible.
 */
@Composable
fun <T> Paginated(
    items: List<T>,
    page: Int,
    onPageChange: (page: Int) -> Unit,
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
    val itemsPerPage = size.width * size.height
    val pageCount = if (itemsPerPage == 0) 1 else (-((-items.size).floorDiv(itemsPerPage))).coerceAtLeast(1)
    val coercedPage = page.coerceIn(0, pageCount - 1)
    Box(Modifier.fillMaxSize()) {
        val start = coercedPage * itemsPerPage
        val end = (coercedPage + 1) * itemsPerPage
        val pageItems = remember(items, start, end) {
            if (start < 0 || start >= items.size) emptyList()
            else items.subList(start, end.coerceAtMost(items.size))
        }
        NavbarLayout(
            position = navbarPosition,
            navbar = {
                NavbarButtons(navbarPosition, navbarBackground) {
                    if (coercedPage > 0) Box(Modifier.clickable { onPageChange(coercedPage - 1) }) {
                        previousButton()
                    }
                    else Spacer(1, 1)
                    if (end < items.size) Box(Modifier.clickable { onPageChange(coercedPage + 1) }) {
                        nextButton()
                    }
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

@Composable
inline fun NavbarButtons(
    navbarPosition: NavbarPosition,
    background: ItemStack?,
    crossinline content: @Composable () -> Unit,
) {
    val navbarSize =
        if (navbarPosition.isVertical()) Modifier.fillMaxHeight().width(1)
        else Modifier.fillMaxWidth().height(1)

    Box(modifier = navbarSize) {
        if (background != null)
            Item(background, modifier = Modifier.fillMaxSize())

        if (navbarPosition.isVertical())
            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceAround) { content() }
        else
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) { content() }
    }
}


enum class NavbarPosition {
    START, END, TOP, BOTTOM;

    fun isVertical() = this == START || this == END
    fun isHorizontal() = this == TOP || this == BOTTOM
}

@Composable
fun NavbarLayout(
    position: NavbarPosition,
    navbar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    when (position) {
        NavbarPosition.START -> Row {
            navbar()
            content()
        }

        NavbarPosition.END -> Box {
            Box(Modifier.padding(end = 1)) {
                content()
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                navbar()
            }
        }

        NavbarPosition.TOP -> Column {
            navbar()
            content()
        }

        NavbarPosition.BOTTOM -> Box {
            Box(Modifier.padding(bottom = 1)) {
                content()
            }
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomStart) {
                navbar()
            }
        }
    }
}
