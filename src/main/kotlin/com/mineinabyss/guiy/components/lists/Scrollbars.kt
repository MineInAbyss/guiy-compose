package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.modifiers.background
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.modifiers.drawWithContent
import me.dvyy.compose.mini.layout.jetpack.Arrangement
import me.dvyy.compose.mini.layout.jetpack.Box
import me.dvyy.compose.mini.layout.jetpack.Column
import me.dvyy.compose.mini.layout.jetpack.Row
import me.dvyy.compose.mini.layout.modifiers.fillMaxHeight
import me.dvyy.compose.mini.layout.modifiers.fillMaxWidth
import me.dvyy.compose.mini.layout.modifiers.padding
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Material

@Composable
internal fun InvisibleBox(
    isVisible: Boolean,
    modifier: Modifier = Modifier.Companion,
    content: @Composable () -> Unit,
) = Box(modifier.drawWithContent { if (isVisible) drawContent() }) {
    content()
}

@Composable
fun HorizontalScrollbar(
    state: ScrollableState,
    modifier: Modifier = Modifier.fillMaxWidth().background(Material.GRAY_STAINED_GLASS_PANE).padding(horizontal = 1.dp),
    hideWhenNoPage: Boolean = true,
    nextButton: @Composable () -> Unit = {
        Item(Material.RED_CONCRETE, "Next", modifier = Modifier.clickable { state.nextPage() })
    },
    prevButton: @Composable () -> Unit = {
        Item(Material.RED_CONCRETE, "Previous", modifier = Modifier.clickable { state.previousPage() })
    },
) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        InvisibleBox(!hideWhenNoPage || state.page != 0) { prevButton() }
        InvisibleBox(!hideWhenNoPage || state.page != state.pageMax - 1) { nextButton() }
    }
}

@Composable
fun VerticalScrollbar(
    state: ScrollableState,
    modifier: Modifier = Modifier.fillMaxHeight().background(Material.GRAY_STAINED_GLASS_PANE).padding(vertical = 1.dp),
    hideWhenNoPage: Boolean = true,
    nextButton: @Composable () -> Unit = {
        Item(Material.RED_CONCRETE, "Next", modifier = Modifier.clickable { state.nextPage() })
    },
    prevButton: @Composable () -> Unit = {
        Item(Material.RED_CONCRETE, "Previous", modifier = Modifier.clickable { state.previousPage() })
    },
) {
    Column(modifier, verticalArrangement = Arrangement.SpaceBetween) {
        InvisibleBox(!hideWhenNoPage || state.page != 0) { prevButton() }
        InvisibleBox(!hideWhenNoPage || state.page != state.pageMax - 1) { nextButton() }
    }
}