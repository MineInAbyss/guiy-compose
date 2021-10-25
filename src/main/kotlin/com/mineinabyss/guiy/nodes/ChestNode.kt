package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.MAX_CHEST_HEIGHT
import com.mineinabyss.guiy.inventory.MAX_CHEST_WIDTH
import org.bukkit.Bukkit
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal class ChestInventory(
    _height: Int,
    _title: String,
) : InventoryCanvas() {
    val width = MAX_CHEST_HEIGHT
    var height: Int = _height
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
    override fun createInventory() = Bukkit.createInventory(this, MAX_CHEST_HEIGHT * height, title)

    override fun set(x: Int, y: Int, item: ItemStack?) {
        if (!updateRunning && x in 0 until width && y in 0 until height)
            inventory.setItem(y * width + x, item)
    }

    override fun processClick(clickType: ClickType, slot: Int) {
        processClick(slot % MAX_CHEST_WIDTH, slot / MAX_CHEST_WIDTH)
    }
}
