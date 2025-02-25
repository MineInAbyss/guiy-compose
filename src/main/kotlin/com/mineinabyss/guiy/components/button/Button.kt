package com.mineinabyss.guiy.components.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.mineinabyss.guiy.components.canvases.LocalInventory
import com.mineinabyss.guiy.layout.Box
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.click.clickable
import org.bukkit.entity.Player

@Composable
fun Button(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    playSound: Boolean = true,
    sounds: InteractionSounds = LocalInteractionSounds.current,
    content: @Composable (enabled: Boolean) -> Unit,
) {
    val inv = LocalInventory.current
    Box(modifier.clickable { //TODO clickable should pass player
        val viewers = inv.viewers.filterIsInstance<Player>()
        if (playSound) {
            if (enabled) viewers.forEach { it.playSound(it.location, sounds.enable, 1f, 1f) }
            else viewers.forEach { it.playSound(it.location, sounds.disable, 1f, 1f) }
        }
        if (enabled) onClick()
    }) {
        content(enabled)
    }
}
