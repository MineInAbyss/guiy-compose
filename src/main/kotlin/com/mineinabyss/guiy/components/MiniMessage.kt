package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.idofront.textcomponents.miniMsg
import net.kyori.adventure.text.Component


@Composable
fun rememberMiniMsg(text: String) = remember(text) { text.miniMsg() }

@Composable
fun rememberMiniMsg(vararg lines: String): List<Component> = remember(lines.toList()) { lines.toList().map { it.miniMsg() } }
