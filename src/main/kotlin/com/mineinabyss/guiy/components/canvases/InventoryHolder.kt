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
import com.mineinabyss.idofront.nms.entities.title
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryCloseEvent

val LocalInventoryHolder = compositionLocalOf<GuiyInventoryHolder> { error("No local inventory holder defined") }

/**
 * Remembers a [GuiyInventoryHolder] which inventories created inside this scope will use to focus their desired
 * Minecraft inventory for players. The holder also manages cases where the player either manually, or via a plugin
 * closes or changes their focused inventory. It may exit, reopen the inventory, or perform other actions when
 * such an event happens.
 */
@Composable
fun InventoryHolder(
    initialViewers: Set<Player> = emptySet(),
    onViewersChange: (Set<Player>) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val holder = rememberInventoryHolder(initialViewers)

    val viewers by holder.viewers.collectAsState()
    val inventory = holder.activeInventory

    remember(viewers) { onViewersChange(viewers) } // Notify callback when viewers change

    // Close inventory when disposing
    DisposableEffect(holder) {
        onDispose {
            guiyPlugin.launch { holder.inventory.close() }
        }
    }

    // TODO we want better detection of when a player closes an inventory in a way we didn't track,
    //  ex. when a plugin closes it for them, they disconnect, or any other edge cases.
    //  This gets complicated as we might be managing multiple Inventory instances (ex. moving player from Chest -> Anvil, or in the future, dialogs)
    // In the meantime, we just remove offline players from viewers to prevent creeping new snapshots in most cases.
    LaunchedEffect(viewers) {
        withContext(guiyPlugin.minecraftDispatcher) {
            while (true) {
                holder.removeViewers(viewers.filter { !it.isOnline })
                viewers.filter { it.isOnline }.forEach { holder.openInventoryFor(it) }
                delay(1000)
            }
        }
    }

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
                holder.openInventoryFor(it)
            }

            if (inventory.title != null) viewers.forEach { it.openInventory.title(inventory.title) }
        }
    }

    CompositionLocalProvider(LocalInventoryHolder provides holder) {
        content()
    }
}

@Composable
fun rememberInventoryHolder(
    initialViewers: Set<Player> = emptySet(),
): GuiyInventoryHolder {
    val clickHandler = LocalClickHandler.current
    val owner = LocalGuiyOwner.current
    val backDispatcher = LocalBackGestureDispatcher.current

    return remember(clickHandler) {
        object : GuiyInventoryHolder(initialViewers) {
            override fun processClick(scope: ClickScope, event: Cancellable) {
                clickHandler.processClick(scope)
            }

            override fun onClose(player: Player) {
                val inventory = activeInventory ?: return
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
            }

            override fun closeIfNoLongerViewing(player: Player) {
//                guiyPlugin.launch {
//                    delay(1.ticks)
//                    if (player.openInventory.topInventory != inventory) {
//                        removeViewers(listOf(player))
//                    }
//                }
            }
        }
    }
}
