package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.INVENTORY_WIDTH
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

internal class ChestInventory(
    val _height: Int,
    val _title: String,
) : InventoryNode() {
    override var width
        get() = INVENTORY_WIDTH
        set(value) {}
    override var height: Int = _height
        set(value) {
            field = value
            updateInventory()
        }

    var title: String = _title
        set(value) {
            field = value
            updateInventory()
        }

    override var activeInventory: Inventory = createInventory()
    override fun createInventory() = Bukkit.createInventory(this, INVENTORY_WIDTH * height, title)

    override fun set(x: Int, y: Int, item: ItemStack?) {
        inventory.setItem(y * INVENTORY_WIDTH + x, item)
    }

    override fun clear(left: Int, top: Int, right: Int, bottom: Int) {
        for (x in left..right)
            for (y in top..bottom)
                inventory.clear(y * INVENTORY_WIDTH + x)
    }

    override fun processClick(slot: Int) {
        processClick(slot % INVENTORY_WIDTH, slot / INVENTORY_WIDTH)
    }
}
