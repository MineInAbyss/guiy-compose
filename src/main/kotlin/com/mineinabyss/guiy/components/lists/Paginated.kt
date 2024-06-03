package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Spacer
import com.mineinabyss.guiy.jetpack.Arrangement
import com.mineinabyss.guiy.layout.Box
import com.mineinabyss.guiy.layout.Column
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.layout.Size
import com.mineinabyss.guiy.modifiers.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Composable
fun <T> Paginated(
    items: List<T>,
    page: Int,
    nextButton: @Composable () -> Unit,
    previousButton: @Composable () -> Unit,
    navbarPosition: NavbarPosition = NavbarPosition.BOTTOM,
    content: @Composable (page: List<T>) -> Unit,
) {
    var size by remember { mutableStateOf(Size(0, 0)) }
    val itemsPerPage = size.width * size.height
    Box(Modifier.fillMaxSize()) {
        val start = page * itemsPerPage
        val end = (page + 1) * itemsPerPage
        val pageItems = remember(start, end) {
            if (start < 0) emptyList()
            else items.subList(start, end.coerceAtMost(items.size))
        }
        NavbarLayout(
            position = navbarPosition,
            navbar = {
                NavbarButtons(navbarPosition) {
                    if (page > 0) previousButton()
                    else Spacer(1, 1)
                    if (end < items.size) nextButton()
                    else Spacer(1, 1)
                }
            },
            content = {
                Box(Modifier.fillMaxSize().onSizeChanged {
                    size = it
                }) {
                    content(pageItems)
                }
            }
        )
    }
}

@Composable
private inline fun NavbarButtons(
    navbarPosition: NavbarPosition,
    background: ItemStack = ItemStack(Material.BLACK_STAINED_GLASS),
    crossinline content: @Composable () -> Unit
) {
    val navbarSize =
        if (navbarPosition.isVertical()) Modifier.fillMaxHeight().width(1)
        else Modifier.fillMaxWidth().height(1)

    Box(modifier = navbarSize) {
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
        NavbarPosition.START ->
            Row {
                navbar()
                content()
            }

        NavbarPosition.END ->
            Row {
                content()
                navbar()
            }

        NavbarPosition.TOP ->
            Column {
                navbar()
                content()
            }

        NavbarPosition.BOTTOM ->
            Column {
                content()
                navbar()
            }
    }
}
