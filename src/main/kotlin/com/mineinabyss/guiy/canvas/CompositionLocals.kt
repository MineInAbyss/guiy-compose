package com.mineinabyss.guiy.canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import com.mineinabyss.guiy.components.canvases.LocalInventoryHolder

val LocalGuiyOwner: ProvidableCompositionLocal<GuiyOwner> =
    staticCompositionLocalOf { error("No provider for GuiyOwner") }

val CurrentPlayer @Composable get() = LocalInventoryHolder.current.viewers.collectAsState().value.single()
