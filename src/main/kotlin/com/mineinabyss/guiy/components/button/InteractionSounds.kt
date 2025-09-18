package com.mineinabyss.guiy.components.button

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import org.bukkit.Sound

@Immutable
data class InteractionSounds(
    val enable: Sound = Sound.ITEM_ARMOR_EQUIP_GENERIC,
    val disable: Sound = Sound.BLOCK_LEVER_CLICK,
)

val LocalInteractionSounds = staticCompositionLocalOf { InteractionSounds() }
