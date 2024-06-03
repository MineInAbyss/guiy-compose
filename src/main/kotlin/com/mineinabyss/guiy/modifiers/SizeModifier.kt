package com.mineinabyss.guiy.modifiers

import androidx.compose.runtime.Stable
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class SizeModifier(
    val constraints: Constraints
) : Modifier.Element<SizeModifier>, LayoutChangingModifier {
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

    override fun modifyLayoutConstraints(constraints: Constraints): Constraints {
        return SizeModifier(constraints).mergeWith(this).constraints
    }
}

data class HorizontalFillModifier(
    val percent: Double
) : Modifier.Element<HorizontalFillModifier>, LayoutChangingModifier {
    override fun mergeWith(other: HorizontalFillModifier) = other

    override fun modifyLayoutConstraints(constraints: Constraints): Constraints {
        val fillWidth = (constraints.minWidth + percent * (constraints.maxWidth - constraints.minWidth)).roundToInt()
        return constraints.copy(
            minWidth = fillWidth,
            maxWidth = fillWidth
        )
    }
}

data class VerticalFillModifier(
    val percent: Double
) : Modifier.Element<VerticalFillModifier>, LayoutChangingModifier {
    override fun mergeWith(other: VerticalFillModifier) = other

    override fun modifyLayoutConstraints(constraints: Constraints): Constraints {
        val fillHeight =
            (constraints.minHeight + percent * (constraints.maxHeight - constraints.minHeight)).roundToInt()
        return constraints.copy(
            minHeight = fillHeight,
            maxHeight = fillHeight
        )
    }
}

/** Forces element width to a percentage between min and max width constraints */
@Stable
fun Modifier.fillMaxWidth(percent: Double = 1.0) = then(HorizontalFillModifier(percent))

/** Forces element height to a percentage between min and max height constraints */
@Stable
fun Modifier.fillMaxHeight(percent: Double = 1.0) = then(VerticalFillModifier(percent))

/** Forces element width and height to a percentage between min and max width and height constraints */
@Stable
fun Modifier.fillMaxSize(percent: Double = 1.0) = then(HorizontalFillModifier(percent)).then(VerticalFillModifier(percent))

/**
 * Sets min and max, width and height constraints for this element.
 */
@Stable
fun Modifier.sizeIn(
    minWidth: Int = 0,
    maxWidth: Int = Integer.MAX_VALUE,
    minHeight: Int = 0,
    maxHeight: Int = Integer.MAX_VALUE,
) = then(SizeModifier(Constraints(minWidth, maxWidth, minHeight, maxHeight)))

/** Sets identical min/max width and height constraints for this element. */
@Stable
fun Modifier.size(width: Int, height: Int) = then(sizeIn(width, width, height, height))

/** Sets identical min/max width constraints for this element. */
@Stable
fun Modifier.width(width: Int) = then(sizeIn(width, width, 0, Integer.MAX_VALUE))

/** Sets identical min/max height constraints for this element. */
@Stable
fun Modifier.height(height: Int) = then(sizeIn(0, Integer.MAX_VALUE, height, height))
