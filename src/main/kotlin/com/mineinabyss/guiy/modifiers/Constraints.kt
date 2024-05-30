package com.mineinabyss.guiy.modifiers

import androidx.compose.runtime.Immutable

@Immutable
data class Constraints(
    val minWidth: Int = 0,
    val maxWidth: Int = Int.MAX_VALUE,
    val minHeight: Int = 0,
    val maxHeight: Int = Int.MAX_VALUE
)
