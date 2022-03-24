package com.mineinabyss.guiy.components.canvases

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.inventory.GuiyOwner
import com.mineinabyss.guiy.inventory.LocalClickHandler
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.modifiers.ClickScope
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.nodes.InventoryCloseScope
import com.mineinabyss.guiy.nodes.StaticMeasurePolicy
import com.okkero.skedule.schedule
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

//val LocalHolder: ProvidableCompositionLocal<GuiyInventoryHolder> =
//    staticCompositionLocalOf { error("No local holder defined") }

val LocalInventory: ProvidableCompositionLocal<Inventory> =
    compositionLocalOf { error("No local inventory defined") }

@Composable
inline fun rememberInventoryHolder(
    viewers: Set<Player>,
    crossinline onClose: InventoryCloseScope.(Player) -> Unit = {},
): GuiyInventoryHolder {
    val clickHandler = LocalClickHandler.current
    return remember(clickHandler) {
        object : GuiyInventoryHolder() {
            override fun processClick(slot: Int, clickType: ClickType) {
                val scope = object : ClickScope {
                    override val clickType: ClickType = clickType
                    override val slot: Int = slot
                    override val item: ItemStack? = inventory.getItem(slot)
                }

                clickHandler.processClick(scope, slot, clickType)
            }

            override fun onClose(player: Player) {
                val scope = object : InventoryCloseScope {
                    override fun reopen() {

                        viewers.filter { it.openInventory.topInventory != inventory }
                            .forEach { it.openInventory(inventory) }
                    }
                }
                guiyPlugin.schedule {
                    waitFor(1)
                    onClose.invoke(scope, player)
                }
            }
        }
    }
}

@Composable
fun GuiyOwner.Inventory(
    inventory: Inventory,
    viewers: Set<Player>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    // Close inventory when it switches to a new one
    DisposableEffect(inventory) {
        onDispose {
            guiyPlugin.schedule {
                inventory.close()
            }
        }
    }
    // Manage opening inventory for new viewers or when inventory changes
    LaunchedEffect(viewers, inventory) {
        val oldViewers = inventory.viewers.toSet()

        guiyPlugin.schedule {
            // Close inventory for removed viewers
            (oldViewers - viewers).forEach {
                it.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
            }

            // Open inventory for new viewers
            (viewers - oldViewers).forEach {
                it.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
                it.openInventory(inventory)
            }
        }
    }

    inventory.clear() //TODO check if this works
    CompositionLocalProvider(LocalInventory provides inventory) {
        Layout(
            measurePolicy = StaticMeasurePolicy,
            modifier = modifier,
            content = content,
        )
    }
}
