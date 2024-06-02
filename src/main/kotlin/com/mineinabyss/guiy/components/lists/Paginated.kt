package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Spacer
import com.mineinabyss.guiy.layout.Box
import com.mineinabyss.guiy.layout.Column
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.layout.alignment.Alignment
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.fillMaxHeight
import com.mineinabyss.guiy.modifiers.fillMaxSize
import com.mineinabyss.guiy.modifiers.fillMaxWidth
import org.bukkit.Material

@Composable
fun <T> Paginated(
    items: List<T>,
    page: Int,
    itemsPerPage: Int,
    nextButton: @Composable () -> Unit,
    previousButton: @Composable () -> Unit,
    navbarPosition: NavbarPosition = NavbarPosition.BOTTOM,
    content: @Composable (page: List<T>) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        val start = page * itemsPerPage
        val end = (page + 1) * itemsPerPage
        if (start >= 0) {
            val pageItems = remember(start, end) { items.subList(start, end.coerceAtMost(items.size)) }
            content(pageItems)
        }
        val alignment = when (navbarPosition) {
            NavbarPosition.START -> Alignment.CenterStart
            NavbarPosition.END -> Alignment.CenterEnd
            NavbarPosition.TOP -> Alignment.TopCenter
            NavbarPosition.BOTTOM -> Alignment.BottomCenter
        }
        Box(contentAlignment = alignment, modifier = Modifier.fillMaxSize()) {
            when (navbarPosition) {
                NavbarPosition.START, NavbarPosition.END -> {
                    Item(Material.GRAY_STAINED_GLASS_PANE, hideTooltip = true, modifier = Modifier.fillMaxHeight())
                    Column {
                        if (page > 0) previousButton()
                        else Spacer(height = 1)
                        Spacer(height = 1)
                        if (end < items.size) nextButton()
                        else Spacer(height = 1)
                    }

                }

                NavbarPosition.TOP, NavbarPosition.BOTTOM -> {
                    Item(Material.GRAY_STAINED_GLASS_PANE, hideTooltip = true, modifier = Modifier.fillMaxWidth())
                    Row {
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
}

enum class NavbarPosition {
    START, END, TOP, BOTTOM
}
