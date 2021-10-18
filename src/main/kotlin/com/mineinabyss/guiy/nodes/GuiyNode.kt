package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas

internal sealed class GuiyNode {
    var width = 0

    //        set(value) {
//            println("Set width to $value")
//            field = value
//        }
    var height = 0

    //        set(value) {
//            println("Set height to $value")
//            field = value
//        }

    var x = 0
    var y = 0

    abstract fun measure()
    abstract fun layout()

    fun render(canvas: GuiyCanvas) {
        measure()
        layout()
        renderTo(canvas)
    }

    abstract fun renderTo(canvas: GuiyCanvas)
}
