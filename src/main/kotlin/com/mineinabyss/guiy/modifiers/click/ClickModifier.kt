package com.mineinabyss.guiy.modifiers.click

import com.mineinabyss.guiy.modifiers.Modifier

open class ClickModifier(
    val merged: Boolean = false,
    val consumeClick: Boolean,
    val onClick: (ClickScope.() -> Unit),
//    val allowClick: (ClickScope.() -> Boolean)
) : Modifier.Element<ClickModifier> {
    override fun mergeWith(other: ClickModifier) = ClickModifier(
        merged = true,
        consumeClick = consumeClick || other.consumeClick,
        onClick = {
            if (!other.merged)
                onClick()
            other.onClick(this)
        },
        /*allowClick = {
                   if (!other.merged)
                       allowClick()
                   other.allowClick(this)
               }*/
    )
}

fun Modifier.clickable(
    consumeClick: Boolean = false,
//    allowClick: ClickScope.() -> Boolean = { true },
    onClick: ClickScope.() -> Unit
) =
    then(
        ClickModifier(
            consumeClick = consumeClick,
            onClick = onClick, /*allowClick = allowClick*/
        )
    )
