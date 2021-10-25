package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.components.GuiyUIScopeMarker
import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.PositionModifier
import com.okkero.skedule.schedule
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

interface InventoryCloseScope {
    fun reopen()
}

@GuiyUIScopeMarker
object InventoryNodeScope {
    fun Modifier.at(x: Int = 0, y: Int = 0) = then(PositionModifier(x, y))
}

internal abstract class InventoryCanvas: GuiyCanvas, InventoryHolder {
    abstract var activeInventory: Inventory
    var onClose: (InventoryCloseScope.(player: Player) -> Unit)? = null
    var viewers = listOf<Player>()
        set(value) {
            val viewers = field
            (viewers - value).forEach {
                it.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
            }
            guiyPlugin.schedule {
                (value - viewers).forEach {
                    it.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
                    it.openInventory(inventory)
                }
            }
            field = value
        }

    override fun getInventory(): Inventory = activeInventory

    abstract fun processClick(clickType: ClickType, slot: Int)

    fun render() = renderTo(this)

    fun processClick(x: Int, y: Int) = children.forEach { it.processClick(x, y) }

    fun onClose(player: Player) {
        val scope = object : InventoryCloseScope {
            override fun reopen() {
                viewers.filter { it.openInventory.topInventory != inventory }
                    .forEach { it.openInventory(inventory) }
            }
        }
        guiyPlugin.schedule {
            waitFor(1)
            onClose?.invoke(scope, player)
        }
    }

    fun transferViewers(to: Inventory): Inventory {
        viewers.forEach {
            it.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
            it.openInventory(to)
        }
        return to
    }

    abstract fun createInventory(): Inventory

    var updateRunning: Boolean = false
    fun updateInventory() {
        !updateRunning || return
        updateRunning = true
        guiyPlugin.schedule {
            activeInventory = transferViewers(createInventory())
            updateRunning = false
            render()
        }
    }

    override fun clear() {
        inventory.clear()
    }
}
