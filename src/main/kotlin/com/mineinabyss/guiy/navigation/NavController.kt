package com.mineinabyss.guiy.navigation

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.viewmodel.GuiyViewModel
import com.mineinabyss.guiy.viewmodel.viewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed

class NavController() : GuiyViewModel() {
    private val screens = MutableStateFlow(listOf<NavRoute>())
    val route: StateFlow<NavRoute?> = screens
        .map { it.lastOrNull() }
        .stateIn(viewModelScope, WhileSubscribed(stopTimeoutMillis = 5000), null)

    fun popBackStack() = screens.update { it.dropLast(1) }

    /**
     * @return The previous navigation entry in te back stack, or null if there is no previous entry.
      */
    fun getPreviousBackStackEntry() = screens.value.getOrNull(screens.value.lastIndex)

    fun navigate(route: NavRoute) = screens.update { it + route }

    fun <T: Any> navigate(route: T) = navigate(NavRoute.of(route))

    fun reset() {
        screens.update { listOf() }
    }

//    fun refresh() {
//        val screen = screen
//        screens.remove(screen)
//        open(screen ?: default())
//    }
}

@Composable
fun rememberNavController() = viewModel { NavController() }
