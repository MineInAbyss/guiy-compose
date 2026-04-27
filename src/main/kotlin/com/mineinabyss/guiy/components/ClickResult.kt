package com.mineinabyss.guiy.components

data class ClickResult(
    val clickConsumed: Boolean? = null,
) {
    fun mergeWith(other: ClickResult) = ClickResult(
        // Prioritize true > false > null
        clickConsumed = (clickConsumed ?: other.clickConsumed)?.or(other.clickConsumed == true)
    )
}