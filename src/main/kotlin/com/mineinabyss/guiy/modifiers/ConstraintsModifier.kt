package com.mineinabyss.guiy.modifiers

import kotlin.math.max
import kotlin.math.min

class ConstraintsModifier(
    val minWidth: Int = 0,
    val maxWidth: Int = Int.MAX_VALUE,
    val minHeight: Int = 0,
    val maxHeight: Int = Int.MAX_VALUE
) : Modifier.Element

//fun maxOrNull(a: Int?, b: Int?): Int? {
//    if (a == null) return b
//    if (b == null) return a
//    return max(a, b)
//}
//
//fun minOrNull(a: Int?, b: Int?): Int? {
//    if (a == null) return b
//    if (b == null) return a
//    return min(a, b)
//}

fun Modifier.getConstraints() = foldIn(ConstraintsModifier()) { accumulated, mod ->
    if (mod !is ConstraintsModifier) return@foldIn accumulated
    ConstraintsModifier(
        minWidth = max(mod.minWidth, accumulated.minWidth),
        maxWidth = min(mod.maxWidth, accumulated.maxWidth),
        minHeight = max(mod.minHeight, accumulated.minHeight),
        maxHeight = min(mod.maxHeight, accumulated.maxHeight)
    )
}

