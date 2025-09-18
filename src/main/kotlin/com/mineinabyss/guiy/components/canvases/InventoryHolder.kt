package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.mineinabyss.guiy.canvas.LocalClickHandler
import com.mineinabyss.guiy.canvas.LocalGuiyOwner
import com.mineinabyss.guiy.canvas.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.canvas.inventory.InventoryCloseScope
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.navigation.LocalBackGestureDispatcher
import com.mineinabyss.idofront.time.ticks
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryCloseEvent

val LocalInventoryHolder = compositionLocalOf<GuiyInventoryHolder> { error("No local inventory holder defined") }

@Composable
fun InventoryHolder(content: @Composable () -> Unit) {
    val holder = rememberInventoryHolder()
    // Close inventory when disposing
    DisposableEffect(holder) {
        onDispose {
            guiyPlugin.launch { holder.inventory.close() }
        }
    }
    val viewers by LocalGuiyOwner.current.viewers.collectAsState()
    val inventory = holder.activeInventory.collectAsState().value

    // Manage opening inventory for new viewers or when inventory changes
    LaunchedEffect(viewers, inventory) {
        val oldViewers = inventory?.inventory?.viewers?.toSet() ?: return@LaunchedEffect

        withContext(guiyPlugin.minecraftDispatcher) {
            // Close inventory for removed viewers
            (oldViewers - viewers).forEach {
                it.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
            }

            // Open inventory for new viewers
            (viewers - oldViewers).forEach {
                it.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
                it.openInventory(inventory.inventory)
            }
        }
    }

    CompositionLocalProvider(LocalInventoryHolder provides holder) {
        content()
    }
}

@Composable
fun rememberInventoryHolder(): GuiyInventoryHolder {
    val clickHandler = LocalClickHandler.current
    val owner = LocalGuiyOwner.current
    val backDispatcher = LocalBackGestureDispatcher.current
    val viewers by owner.viewers.collectAsState()
    return remember(clickHandler, viewers) {
        object : GuiyInventoryHolder() {
            override fun processClick(scope: ClickScope, event: Cancellable) {
                clickHandler.processClick(scope)
            }

            override fun onClose(player: Player) {
                val inventory = activeInventory.value ?: return
                val scope = object : InventoryCloseScope {
                    override val player = player

                    override fun exit() {
                        owner.exit()
                    }

                    override fun back() {
                        if (backDispatcher.hasActiveListeners()) {
                            backDispatcher.onBack()
                        } else exit()
                    }
                }
                inventory.onClose.invoke(scope)
                //TODO handle both switching from one Inventory to another, as well as when current inventory closed
                // and no inventory is swapped to. Maybe notify GuiyOwner to retry opening inventory?
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
