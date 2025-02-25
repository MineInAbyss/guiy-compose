package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.inventory.LocalClickHandler
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.modifiers.drag.DragScope
import com.mineinabyss.guiy.nodes.InventoryCloseScope
import com.mineinabyss.idofront.time.ticks
import kotlinx.coroutines.delay
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

@Composable
inline fun rememberInventoryHolder(
    viewers: Set<Player>,
    crossinline onClose: InventoryCloseScope.(Player) -> Unit = {},
): GuiyInventoryHolder {
    val clickHandler = LocalClickHandler.current
    val owner = LocalGuiyOwner.current
    return remember(clickHandler) {
        object : GuiyInventoryHolder() {
            override fun processClick(scope: ClickScope, event: Cancellable) {
                clickHandler.processClick(scope)
            }

            override fun processDrag(scope: DragScope) {
                clickHandler.processDrag(scope)
            }

            override fun onClose(player: Player) {
                val scope = object : InventoryCloseScope {
                    override fun reopen() {
                        //TODO don't think this reference updates properly in the remember block
                        viewers.filter { it.openInventory.topInventory != inventory }
                            .forEach { it.openInventory(inventory) }
                    }

                    override fun exit() {
                        owner.exit()
                    }
                }
                onClose.invoke(scope, player)
                closeIfNoLongerViewing(player)
            }

            override fun closeIfNoLongerViewing(player: Player) {
                guiyPlugin.launch {
                    delay(1.ticks)
                    if (player.openInventory.topInventory != inventory) {
                        owner.removeViewers(player)
                    }
                }
            }
        }
    }
}
