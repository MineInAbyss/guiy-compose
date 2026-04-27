package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.layout.jetpack.Column
import me.dvyy.compose.mini.layout.jetpack.Row
import me.dvyy.compose.mini.layout.modifiers.fillMaxHeight
import me.dvyy.compose.mini.layout.modifiers.fillMaxWidth
import me.dvyy.compose.mini.layout.modifiers.onSizeChanged
import me.dvyy.compose.mini.modifier.Modifier

enum class ScrollDirection {
    /** Scroll one line at a time up and down. */
    VERTICAL,

    /** Scroll one line at a time left and right. */
    HORIZONTAL,

    /** Scroll by whole pages at once (i.e. all items on screen move out for the next page) */
    PAGINATED;
}

enum class NavbarPosition {
    START, END, TOP, BOTTOM;

    fun isVertical() = this == START || this == END
    fun isHorizontal() = this == TOP || this == BOTTOM
}

/**
 * A scrollable or paginated list of items, with buttons to go to the next and previous pages.
 *
 * Content must set a size or fillMaxSize Modifier to be visible.
 * Useful in combination with composables like [VerticalScrollbar], [com.mineinabyss.guiy.components.VerticalGrid], etc.
 *
 * @sample com.mineinabyss.guiy.example.gui.PaginatedMenu
 * @sample com.mineinabyss.guiy.example.gui.ScrollingMenu
 */
@Composable
fun <T> Scrollable(
    items: List<T>,
    state: ScrollableState = rememberScrollableState(ScrollDirection.PAGINATED),
    navbarPosition: NavbarPosition = NavbarPosition.BOTTOM,
    scrollbar: @Composable () -> Unit = {
        if (navbarPosition.isVertical()) VerticalScrollbar(state)
        else HorizontalScrollbar(state)
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
    if (navbarPosition.isHorizontal()) Column {
        val isTop = navbarPosition == NavbarPosition.TOP
        if (isTop) scrollbar()
        Box(Modifier.fillMaxWidth().weight(1f).onSizeChanged { state.setSize(it) }) {
            content(pageItems)
        }
        if (!isTop) scrollbar()
    }
    else Row {
        val isStart = navbarPosition == NavbarPosition.START
        if (isStart) scrollbar()
        Box(Modifier.fillMaxHeight().weight(1f).onSizeChanged { state.setSize(it) }) {
            content(pageItems)
        }
        if (!isStart) scrollbar()
    }
}
