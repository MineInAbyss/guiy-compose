package com.mineinabyss.guiy.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import org.bukkit.entity.Player

@Composable
fun rememberViewers() = remember { derivedStateOf { mutableStateListOf<Player>() } }
