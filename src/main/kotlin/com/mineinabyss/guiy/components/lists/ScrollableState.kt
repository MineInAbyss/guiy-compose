package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntSize

@Composable
fun rememberScrollableState(
    direction: ScrollDirection = ScrollDirection.VERTICAL,
) = remember(direction) { ScrollableState(direction) }

@Stable
class ScrollableState(
    val direction: ScrollDirection = ScrollDirection.PAGINATED,
) {
    private var _itemCount by mutableStateOf(0)

    var itemCount: Int
        get() = _itemCount
        internal set(value) {
            val shrank = value < _itemCount
            _itemCount = value
            if (shrank && page > lastFullPage) page = lastFullPage
        }
    var page by mutableStateOf(0)
        internal set

    private val lastFullPage get() = (pageMax - pagesOnScreen).coerceAtLeast(0)

    val pageMax
        get() = if (itemsPerPage == 0) 1
        else (-((-itemCount).floorDiv(itemsPerPage))).coerceAtLeast(1)

    val itemsPerPage
        get() = when (direction) {
            ScrollDirection.PAGINATED -> (size.width * size.height)
            ScrollDirection.VERTICAL -> size.width
            ScrollDirection.HORIZONTAL -> size.height
        }
    val pagesOnScreen
        get() = when (direction) {
            ScrollDirection.PAGINATED -> 1
            ScrollDirection.VERTICAL -> size.height
            ScrollDirection.HORIZONTAL -> size.width
        }

    private var size by mutableStateOf(IntSize(0, 0))

    val start get() = page * itemsPerPage
    val end get() = (page + pagesOnScreen) * itemsPerPage

    fun nextPage() {
        setPage(page + 1)
    }

    fun previousPage() {
        setPage(page - 1)
    }

    /** Moves one page per wheel step, stopping once the last row or column is on screen where [nextPage] keeps going until only one is left */
    fun scroll(delta: Int) {
        if (delta < 0) previousPage()
        else if (page < lastFullPage) nextPage()
    }

    fun setPage(page: Int) {
        this.page = (page).coerceIn(0, pageMax - 1)
    }

    internal fun setSize(size: IntSize) {
        this.size = size
    }
}