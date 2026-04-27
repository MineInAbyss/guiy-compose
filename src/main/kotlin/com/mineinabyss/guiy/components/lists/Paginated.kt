package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.modifiers.background
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.idofront.items.editItemMeta
import me.dvyy.compose.mini.layout.jetpack.*
import me.dvyy.compose.mini.layout.modifiers.fillMaxHeight
import me.dvyy.compose.mini.layout.modifiers.fillMaxWidth
import me.dvyy.compose.mini.layout.modifiers.onSizeChanged
import me.dvyy.compose.mini.layout.modifiers.padding
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Stable
class PaginatedState(
    val direction: ScrollDirection = ScrollDirection.PAGINATED,
) {
    var itemCount by mutableStateOf(0)
        internal set
    var page by mutableStateOf(0)
        internal set
    val pageCount
        get() = if (itemsPerPage == 0) 1
        else (-((-itemCount).floorDiv(itemsPerPage))).coerceAtLeast(1)
    val coercedPage get() = page.coerceIn(0, pageCount - 1)
    fun nextPage() {
        page++
        page = coercedPage
    }
    val itemsPerPage get() = (size.width * size.height)

    private var size by mutableStateOf(IntSize(0, 0))

    val itemsPerLine get() = if (direction == ScrollDirection.VERTICAL) size.width else size.height
    val totalLines = if (direction == ScrollDirection.VERTICAL) size.height else size.width

    val start
        get() = if (direction == ScrollDirection.PAGINATED) coercedPage * itemsPerPage
        else coercedPage * itemsPerLine
    val end
        get() = if (direction == ScrollDirection.PAGINATED) (coercedPage + 1) * itemsPerPage
        else start + (itemsPerLine * totalLines)

    fun previousPage() {
        page--
        page = coercedPage
    }

    internal fun setSize(size: IntSize) {
        this.size = size
    }
}

/**
 * A paginated list of items, with buttons to go to the next and previous pages.
 *
 * Content must set a size or fillMaxSize Modifier to be visible.
 */
@Composable
fun <T> Paginated(
    items: List<T>,
    state: PaginatedState = remember { PaginatedState() },
    nextButton: @Composable (Modifier) -> Unit,
    previousButton: @Composable (Modifier) -> Unit,
    navbarPosition: NavbarPosition = NavbarPosition.BOTTOM,
    navbarBackground: ItemStack? = remember {
        ItemStack(Material.GRAY_STAINED_GLASS_PANE).editItemMeta {
            isHideTooltip = true
        }
    },
    content: @Composable (page: List<T>) -> Unit,
) {
    state.itemCount = items.size
    val start = state.start
    val end = state.end
    val pageItems = remember(items, start, end) {
        if (start < 0 || start >= items.size) emptyList()
        else items.subList(start, end.coerceAtMost(items.size))
    }

    //TODO switch to min intrinsic width
//    if (navbarPosition == NavbarPosition.BOTTOM) {
    Column {
        Box(Modifier.fillMaxWidth().weight(1f).onSizeChanged { state.setSize(it) }) {
            content(pageItems)
        }
        Row(
            Modifier.fillMaxWidth().background(Material.GRAY_STAINED_GLASS_PANE).padding(horizontal = 1.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Item(Material.RED_CONCRETE, "Previous", modifier = Modifier.clickable { state.previousPage() })
            Item(Material.RED_CONCRETE, "Next", modifier = Modifier.clickable { state.nextPage() })
        }
    }
//    }

//        NavbarButtons(NavbarPosition.BOTTOM/*navbarPosition*/, navbarBackground) {
//            if (coercedPage > 0) previousButton(Modifier.clickable { onPageChange(coercedPage - 1) })
//            else Spacer(1.dp, 1.dp)
//            if (end < items.size) nextButton(Modifier.clickable { onPageChange(coercedPage + 1) })
//            else Spacer(1.dp, 1.dp)
//        }
    // Extract original size of contents
//    Box(Modifier.onSizeChanged {
//        size = it
//    }) {
//    }

    // Clear out the previous Box
//    Box(Modifier.onSizeChanged {
//        clearSize = it
//    }.fillMaxSize()) {
//        VerticalGrid() {
//            MutableList(clearSize.width * clearSize.height) { Item(null) }
//        }
//    }

//    NavbarLayout(
//        position = navbarPosition,
//        navbar = {
//            NavbarButtons(navbarPosition, navbarBackground) {
//                if (coercedPage > 0) previousButton(Modifier.clickable { onPageChange(coercedPage - 1) })
//                else Spacer(1.dp, 1.dp)
//                if (end < items.size) nextButton(Modifier.clickable { onPageChange(coercedPage + 1) })
//                else Spacer(1.dp, 1.dp)
//            }
//        },
//        content = {
//            // Actually render the correct amount of items into a box that can fit them including offsets
//            Box(Modifier.fillMaxSize()) {
//                content(pageItems)
//            }
//        }
//    )
}
//
//@Composable
//private fun NavbarButtons(
//    navbarPosition: NavbarPosition,
//    background: ItemStack?,
//    crossinline content: @Composable () -> Unit,
//) {
//    val navbarSize =
//        if (navbarPosition.isVertical()) Modifier.fillMaxHeight().width(1.dp)
//        else Modifier.fillMaxWidth().height(1.dp)
//
//    Box(modifier = navbarSize) {
//        if (background != null)
//            Item(background, modifier = Modifier.fillMaxSize())
//
//        if (navbarPosition.isVertical())
//            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceAround) { content() }
//        else
//            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) { content() }
//    }
//}


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
            Box(Modifier.padding(end = 1.dp)) {
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
            Box(Modifier.padding(bottom = 1.dp)) {
                content()
            }
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomStart) {
                navbar()
            }
        }
    }
}
