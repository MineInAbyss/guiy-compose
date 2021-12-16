package com.mineinabyss.guiy.modifiers

data class Constraints(
    val minWidth: Int = 0,
    val maxWidth: Int = Int.MAX_VALUE,
    val minHeight: Int = 0,
    val maxHeight: Int = Int.MAX_VALUE
)
