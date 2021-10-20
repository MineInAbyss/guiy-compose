package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.guiyPlugin
import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.okkero.skedule.schedule
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

internal abstract class InventoryNode : GuiyNode(), ChildHoldingNode, GuiyCanvas, InventoryHolder {
    abstract var activeInventory: Inventory
    var onClose: ((player: Player) -> Unit)? = null
    var viewers = listOf<Player>()
        set(value) {
            val viewers = field
            (viewers - value).forEach { it.closeInventory() }
            guiyPlugin.schedule {
                (value - viewers).forEach { it.openInventory(inventory) }
            }
            field = value
        }

    override fun getInventory(): Inventory = activeInventory

    abstract fun processClick(slot: Int)

    override val children = mutableListOf<GuiyNode>()

    override fun measure() = children.forEach { it.measure() }

    override fun layout() = children.forEach { it.layout() }

    fun render() = renderTo(this)

    override fun renderTo(canvas: GuiyCanvas) = children.forEach { it.renderTo(canvas) }

    override fun processClick(x: Int, y: Int) = children.forEach { it.processClick(x, y) }

    fun onClose(player: Player) = onClose?.invoke(player)

    fun transferViewers(to: Inventory): Inventory {
        viewers.forEach {
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
            render()
            updateRunning = false
        }
    }
}
