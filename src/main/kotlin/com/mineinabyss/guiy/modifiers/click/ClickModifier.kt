package com.mineinabyss.guiy.modifiers.click

import com.mineinabyss.guiy.modifiers.Modifier

open class ClickModifier(
    val merged: Boolean = false,
    val cancelClickEvent: Boolean,
    val onClick: (ClickScope.() -> Unit),
//    val allowClick: (ClickScope.() -> Boolean)
) : Modifier.Element<ClickModifier> {
    override fun mergeWith(other: ClickModifier) = ClickModifier(
        merged = true,
        cancelClickEvent = cancelClickEvent || other.cancelClickEvent,
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
    cancelClickEvent: Boolean = true,
//    allowClick: ClickScope.() -> Boolean = { true },
    onClick: ClickScope.() -> Unit
) =
    then(
        ClickModifier(
            cancelClickEvent = cancelClickEvent,
            onClick = onClick, /*allowClick = allowClick*/
        )
    )
