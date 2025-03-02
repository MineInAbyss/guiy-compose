package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.*
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.inventory.InventoryCloseScope
import com.mineinabyss.guiy.inventory.LocalClickHandler
import com.mineinabyss.guiy.inventory.LocalGuiyOwner
import com.mineinabyss.guiy.modifiers.click.ClickScope
import com.mineinabyss.guiy.modifiers.drag.DragScope
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
    val viewers by owner.viewers.collectAsState()
    return remember(clickHandler, viewers) {
        object : GuiyInventoryHolder() {
            override fun processClick(scope: ClickScope, event: Cancellable) {
                clickHandler.processClick(scope)
            }

            override fun processDrag(scope: DragScope) {
                clickHandler.processDrag(scope)
            }

            override fun onClose(player: Player) {
                val inventory = activeInventory.value ?: return
                val scope = object : InventoryCloseScope {
                    override val player = player

                    override fun exit() {
                        owner.exit()
                    }
                }
                inventory.onClose.invoke(scope)
                if (!owner.exitScheduled) guiyPlugin.launch {
                    delay(1.ticks)
                    viewers.filter { it.openInventory.topInventory != inventory }
                        .forEach { it.openInventory(inventory.inventory) }
                }
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
