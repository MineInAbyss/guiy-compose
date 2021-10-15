package com.mineinabyss.guiy.nodes

import org.bukkit.inventory.InventoryHolder

internal class BoxNode: GuiyNode() {
    val children = mutableListOf<GuiyNode>()

    fun render() {
        println("Width ${children.map { it.width }.maxOrNull()}")
    }
    override fun renderTo(inventory: InventoryHolder) {
        TODO("Not yet implemented")
    }
}
