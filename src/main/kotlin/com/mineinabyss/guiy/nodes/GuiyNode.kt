package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.Bounds
import org.bukkit.inventory.InventoryHolder

internal sealed class GuiyNode {
    var width = 0
        set(value) {
            println("Set width to $value")
            field = value
        }
    var height = 0
        set(value) {
            println("Set height to $value")
            field = value
        }
    var x = 0
    var y = 0

    abstract fun renderTo(inventory: InventoryHolder)
}
