package com.mineinabyss.guiy.modifiers

import com.mineinabyss.guiy.layout.Size


class OnSizeChangedModifier(
    val merged: Boolean = false,
    val onSizeChanged: (Size) -> Unit
) : Modifier.Element<OnSizeChangedModifier> {
    override fun mergeWith(other: OnSizeChangedModifier) = OnSizeChangedModifier(merged = true) { size ->
        if (!other.merged)
            onSizeChanged(size)
        other.onSizeChanged(size)
    }
}

fun Modifier.onSizeChanged(onSizeChanged: (Size) -> Unit) = then(
    OnSizeChangedModifier(onSizeChanged = onSizeChanged)
)
