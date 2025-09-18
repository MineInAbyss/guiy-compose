package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

@Composable
fun <T: Any> NavHost(
    navController: NavController,
    startDestination: T,
    builder: NavGraphBuilder.() -> Unit,
) {
    BackHandler { navController.popBackStack() }
    val graph = remember(builder, navController, startDestination) {
        NavGraphBuilder().apply(builder).build(NavRoute.of(startDestination))
    }
    val route = navController.route.collectAsState().value ?: graph.start
    graph.getDestination(route)?.invoke(route) ?: error("No navigation destination for ${route.route} was found")
}
