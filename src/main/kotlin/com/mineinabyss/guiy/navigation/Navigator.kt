package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed

class NavController() {

}

class NavGraphBuilder() {

}

@Composable
fun NavHost(
    navController: NavController,
    builder: NavGraphBuilder.() -> Unit,
) {

}

class Navigator<T>(
    val scope: CoroutineScope,
    val default: () -> T,
) {
    private val screens = MutableStateFlow(listOf<T>())
    private val screen: StateFlow<T?> =
        screens.map { it.lastOrNull() }.stateIn(scope, WhileSubscribed(stopTimeoutMillis = 5000), null)

    init {
        open(default())
    }

    fun back() = screens.update { it.dropLast(1) }
    fun open(screen: T) = screens.update { it + screen }

    fun reset() {
        screens.update { listOf() }
        open(default())
    }

//    fun refresh() {
//        val screen = screen
//        screens.remove(screen)
//        open(screen ?: default())
//    }

    /**
     * Entrypoint for handling composition based on screen.
     *
     * Includes universal defaults like an Anvil screen.
     */
//    @Composable
//    fun withScreen(players: Set<Player>, onEmpty: () -> Unit, run: @Composable (T) -> Unit) {
//        if (universal.isNotEmpty()) {
//            when (val screen = universal.first()) {
//                is UniversalScreens.Anvil -> LaunchedEffect(screen) {
//                    guiyPlugin.launch {
//                        screen.builder.open(players.first()).inventory
//                    }
//                }
//            }
//        } else screen?.let { run(it) } ?: onEmpty()
//    }
}

@Composable
fun Navigation(content: () -> Unit) {

}

//@Composable
//fun <T> rememberNavigation(default: () -> T) = remember {
//    Navigator(default)
//}
