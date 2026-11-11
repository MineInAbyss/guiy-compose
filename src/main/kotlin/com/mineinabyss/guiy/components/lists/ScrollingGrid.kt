package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mineinabyss.guiy.components.VerticalGrid
import com.mineinabyss.guiy.modifiers.scroll.scrollableWhenEmpty
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.layout.modifiers.size
import me.dvyy.compose.mini.modifier.Modifier

/**
 * A grid of [columns] by [rows] slots that the mouse wheel scrolls through [items] one row at a time, empty slots included.
 * It draws no scrollbar, a [VerticalScrollbarTrack] on the same [state] can sit anywhere beside it
 */
@Composable
fun <T> VerticalScrollingGrid(
    items: List<T>,
    columns: Int,
    rows: Int,
    modifier: Modifier = Modifier,
    state: ScrollableState = rememberScrollableState(ScrollDirection.VERTICAL),
    content: @Composable (T) -> Unit,
) {
    Box(modifier.size(columns.dp, rows.dp)) {
        Scrollable(items, state, NavbarPosition.END, scrollbar = {}) { page ->
            VerticalGrid(Modifier.size(columns.dp, rows.dp).scrollableWhenEmpty()) {
                page.forEach { content(it) }
            }
        }
    }
}
