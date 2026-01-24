package com.mineinabyss.guiy.components.items

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.components.Item

@Composable
fun InvisibleItem(hideTooltip: Boolean = true) {
    if (hideTooltip) Item(LocalItemTheme.current.hiddenTooltip) else Item(LocalItemTheme.current.invisible)
}
