package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Composable

data class NavGraph(
    val destinations: Map<String, @Composable (NavRoute) -> Unit>,
    val start: NavRoute,
) {
    fun getDestination(route: NavRoute) = destinations[route.route]
}
