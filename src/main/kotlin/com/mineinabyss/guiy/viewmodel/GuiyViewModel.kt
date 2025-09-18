package com.mineinabyss.guiy.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.canvas.GuiyUIDispatcher
import com.mineinabyss.guiy.canvas.LocalGuiyOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.reflect.typeOf

abstract class GuiyViewModel {
    val viewModelScope = CoroutineScope(GuiyUIDispatcher.Main + SupervisorJob())
//    fun <T: AutoCloseable> getClosable(key: String) : T? {
//
//    }
//    fun addClosable(key: String, closable: AutoCloseable) {}

    fun close() {
        viewModelScope.cancel()
    }
}

@Composable
inline fun <reified T : GuiyViewModel> viewModel(crossinline provider: () -> T = { error("ViewModel for ${typeOf<T>()} not found!") }): T {
    val owner = LocalGuiyOwner.current
    val type = typeOf<T>()
    return remember {
        owner.getViewModel<T>(type) ?: provider().also { owner.addViewModel(type, it) }
    }
}
