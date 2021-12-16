package com.mineinabyss.guiy.modifiers

import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class SizeModifier(
    val constraints: Constraints
) : Modifier.Element<SizeModifier> {
    override fun mergeWith(other: SizeModifier) = with(constraints) {
        SizeModifier(
            Constraints(
                max(minWidth, other.constraints.minWidth),
                min(maxWidth, other.constraints.maxWidth),
                max(minHeight, other.constraints.minHeight),
                min(maxHeight, other.constraints.maxHeight),
            )
        )
    }
}

data class HorizontalFillModifier(
    val percent: Double
) : Modifier.Element<HorizontalFillModifier> {
    override fun mergeWith(other: HorizontalFillModifier) = other
}

data class VerticalFillModifier(
    val percent: Double
) : Modifier.Element<VerticalFillModifier> {
    override fun mergeWith(other: VerticalFillModifier) = other
}

fun Constraints.applyFill(horizontal: HorizontalFillModifier?, vertical: VerticalFillModifier?): Constraints {
    val fillWidth = if (horizontal == null) null else
        (minWidth + horizontal.percent * (maxWidth - minWidth)).roundToInt()
    val fillHeight = if (vertical == null) null else
        (minHeight + vertical.percent * (maxHeight - minHeight)).roundToInt()

    return Constraints(
        fillWidth ?: minWidth,
        fillWidth ?: maxWidth,
        fillHeight ?: minHeight,
        fillHeight ?: maxHeight
    )
}

fun Modifier.sizeIn(
    minWidth: Int = 0,
    maxWidth: Int = Integer.MAX_VALUE,
    minHeight: Int = 0,
    maxHeight: Int = Integer.MAX_VALUE,
) = then(SizeModifier(Constraints(minWidth, maxWidth, minHeight, maxHeight)))

fun Modifier.size(width: Int, height: Int) = then(sizeIn(width, width, height, height))

fun Modifier.width(width: Int) = then(sizeIn(width, width, 0, Integer.MAX_VALUE))

fun Modifier.height(height: Int) = then(sizeIn(0, Integer.MAX_VALUE, height, height))
