package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas

internal abstract class GuiyNode {
    open var width = 0

    //        set(value) {
//            println("Set width to $value")
//            field = value
//        }
    open var height = 0

    //        set(value) {
//            println("Set height to $value")
//            field = value
//        }

    var x = 0
    var y = 0


    var onClick: (() -> Unit)? = null

    open fun processClick(x: Int, y: Int) {
        onClick?.let { it() }
    }

    abstract fun measure()
    abstract fun layout()

    abstract fun renderTo(canvas: GuiyCanvas)
}
