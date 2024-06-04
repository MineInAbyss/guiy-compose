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

@Composable
fun <T> Scrollable(
    items: List<T>,
    startLine: Int,
    itemsPerLine: Int,
    totalLines: Int,
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
    Box(Modifier.fillMaxSize()) {
        val start = startLine * itemsPerLine
        val end = (startLine + 1) * itemsPerLine * totalLines
        val pageItems = remember(start, end) {
            if (start < 0) emptyList()
            else items.subList(start, end.coerceAtMost(items.size))
        }
        NavbarLayout(
            position = navbarPosition,
            navbar = {
                NavbarButtons(navbarPosition, navbarBackground) {
                    if (startLine > 0) previousButton()
                    //else Spacer(1, 1)
                    if (end < items.size) nextButton()
                    //else Spacer(1, 1)
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
