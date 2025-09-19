package com.mineinabyss.guiy.canvas.inventory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.modifiers.click.ClickScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * A class for Inventory composables to request focus of a certain inventory. They should call [setActiveInventory]
 * on render, such that the last rendered inventory will be considered active, and Guiy can manage showing it to the
 * player.
 */
abstract class GuiyInventoryHolder(initialViewers: Set<Player>) : InventoryHolder {
    /** The last inventory that child composables rendered to, which should be shown to the player. */
    var activeInventory by mutableStateOf<GuiyInventory?>(null)
        private set

    private val _viewers: MutableStateFlow<Set<Player>> = MutableStateFlow(initialViewers)
    val viewers = _viewers.asStateFlow()

    /**
     * A bukkit method for getting the inventory. Will error if nothing has rendered to an inventory but some bukkit
     * consumer tried to get it.
     */
    override fun getInventory(): Inventory =
        activeInventory?.inventory ?: error("Guiy inventory is used in bukkit but has not been rendered yet.")

    //FIXME since this is fired after render, we need to run render once, update this state,
    // callback updates to any observing InventoryHolder composable (I think causing another re-render)
    // then re-run the render again due to a launched effect?
    @JvmName("setActiveInventoryWithSnapshot")
    fun setActiveInventory(inventory: GuiyInventory) = Snapshot.withMutableSnapshot {
        activeInventory = inventory
    }

    abstract fun processClick(scope: ClickScope, event: Cancellable)

    abstract fun onClose(player: Player)

    abstract fun closeIfNoLongerViewing(player: Player)

    fun openInventoryFor(player: HumanEntity) {
        val inventory = activeInventory?.inventory ?: return

        if (player.openInventory.topInventory != inventory) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
            player.openInventory(inventory)
        }
    }

    /** Stops sending UI updates to these Players. If no players remain, the composition automatically exits. */
    fun removeViewers(viewers: Collection<Player>) {
        _viewers.update { it - viewers.toSet() }
    }

    fun addViewers(viewers: Collection<Player>) {
        _viewers.update { it + viewers.toSet() }
    }

    fun close() {
        inventory.viewers.forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
    }
}
