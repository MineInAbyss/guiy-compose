package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.MAX_CHEST_WIDTH
import com.mineinabyss.guiy.layout.MeasurePolicy
import com.mineinabyss.guiy.layout.MeasureResult
import org.bukkit.Bukkit
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal class ChestCanvas(
    _height: Int,
    _title: String,
) : InventoryCanvas() {
    override var measurePolicy: MeasurePolicy
        get() = MeasurePolicy { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            MeasureResult(width, height) {
                placeables.forEach { it.placeAt(0, 0) }
            }
        }
        set(value) {}
    override var width
        get() = MAX_CHEST_WIDTH
        set(value) {}
    override var height: Int = _height
        set(value) {
            if(field != value) {
                field = value
                updateInventory()
            }
        }

    var title: String = _title
        set(value) {
            if(field != value) {
                field = value
                updateInventory()
            }
        }

    override var activeInventory: Inventory = createInventory()
    override fun createInventory() = Bukkit.createInventory(this, width * height, title)

    override fun set(x: Int, y: Int, item: ItemStack?) {
        if (!updateRunning && x in 0 until width && y in 0 until height)
            inventory.setItem(y * width + x, item)
    }

    override fun processClick(slot: Int, clickType: ClickType) {
        processClick(slot % MAX_CHEST_WIDTH, slot / MAX_CHEST_WIDTH, clickType)
    }
}
