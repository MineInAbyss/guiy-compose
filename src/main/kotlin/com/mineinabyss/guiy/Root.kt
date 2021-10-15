package com.mineinabyss.guiy

import androidx.compose.runtime.Composable

interface GuiyScope {
    fun exit()

    fun setContent(content: @Composable () -> Unit)
}
