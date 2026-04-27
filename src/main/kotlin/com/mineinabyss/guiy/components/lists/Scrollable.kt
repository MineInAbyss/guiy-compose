package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntSize
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.VerticalGrid
import com.mineinabyss.idofront.items.editItemMeta
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.layout.modifiers.fillMaxSize
import me.dvyy.compose.mini.layout.modifiers.onSizeChanged
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class ScrollDirection {
    VERTICAL, HORIZONTAL, PAGINATED;
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
    onLineChange: (line: Int) -> Unit,
    scrollDirection: ScrollDirection,
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
    var size by remember { mutableStateOf(IntSize(0, 0)) }
    var clearSize by remember { mutableStateOf(IntSize(0, 0)) }

    val itemsPerLine = if (scrollDirection == ScrollDirection.VERTICAL) size.width else size.height
    val totalLines = if (scrollDirection == ScrollDirection.VERTICAL) size.height else size.width

    Box(Modifier.fillMaxSize().onSizeChanged { println("Exterior box size: $it") }) {
        val start = line * itemsPerLine
        val lineCount = if (itemsPerLine == 0) 1 else (-((-items.size).floorDiv(itemsPerLine))).coerceAtLeast(1)
        val coercedLine = line.coerceIn(0, lineCount - 1)
        Box(Modifier.fillMaxSize()) {
            val start = coercedLine * itemsPerLine
            val end = start + (itemsPerLine * totalLines)
            val pageItems = remember(items, start, end) {
                if (start < 0 || start >= items.size) emptyList()
                else items.subList(start, end.coerceAtMost(items.size))
            }

            // Extract original size of contents
            Box(Modifier.onSizeChanged {
                size = it
            }) {
                content(pageItems)
            }

            // Clear out the previous Box
            Box(Modifier.onSizeChanged {
                clearSize = it
            }.fillMaxSize()) {
                VerticalGrid() {
                    MutableList(clearSize.width * clearSize.height) { Item(null) }
                }
            }

            NavbarLayout(
                position = navbarPosition,
                navbar = {
//                    NavbarButtons(navbarPosition, navbarBackground) {
//                        if (coercedLine > 0) previousButton(Modifier.clickable { onLineChange(coercedLine - 1) })
//                        else Spacer(1.dp, 1.dp)
//                        if (end < items.size) nextButton(Modifier.clickable { onLineChange(coercedLine + 1) })
//                        else Spacer(1.dp, 1.dp)
//                    }
                },
                content = {
                    // Actually render the correct amount of items into a box that can fit them including offsets
                    Box(Modifier.fillMaxSize()) {
                        content(pageItems)
                    }
                }

            )
        }
    }
}