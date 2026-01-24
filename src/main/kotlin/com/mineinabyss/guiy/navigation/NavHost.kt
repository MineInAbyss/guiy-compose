package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.canvas.GuiyOwner
import com.mineinabyss.guiy.canvas.LocalGuiyOwner

@Composable
fun <T: Any> NavHost(
    navController: NavController,
    startDestination: T,
    onEmptyBackStack: (owner: GuiyOwner) -> Unit = { it.exit() },
    builder: NavGraphBuilder.() -> Unit,
) {
    val owner = LocalGuiyOwner.current
    BackHandler { if (navController.getPreviousBackStackEntry() == null) onEmptyBackStack(owner) else navController.popBackStack() }
    val graph = remember(builder, navController, startDestination) {
        NavGraphBuilder().apply(builder).build(NavRoute.of(startDestination))
    }
    val route = navController.route.collectAsState().value ?: graph.start
    graph.getDestination(route)?.invoke(route) ?: error("No navigation destination for ${route.route} was found")
}
