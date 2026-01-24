package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Composable

class NavGraphBuilder {
    val destinations = mutableMapOf<String, @Composable (NavRoute) -> Unit>()
    fun build(startDestination: NavRoute): NavGraph = NavGraph(
        destinations, startDestination
    )
}

inline fun <reified T> NavGraphBuilder.composable(crossinline content: @Composable (T) -> Unit) {
    destinations[NavRoute.routeFor(T::class)] = {
        content(it.data as T)
    }
}
