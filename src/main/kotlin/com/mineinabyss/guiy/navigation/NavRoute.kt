package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Immutable
import kotlin.reflect.KClass

@Immutable
data class NavRoute(
    val route: String,
    val data: Any?,
) {
    companion object {
        fun routeFor(kClass: KClass<*>) = "/${kClass.qualifiedName}"
        fun of(data: Any) = NavRoute(routeFor(data::class), data)
    }
}
