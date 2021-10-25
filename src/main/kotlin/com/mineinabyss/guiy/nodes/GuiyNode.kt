package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.layout.Measurer
import com.mineinabyss.guiy.layout.Placer
import com.mineinabyss.guiy.modifiers.Modifier

interface GuiyNode {
    var measurer: Measurer
    var placer: Placer
    var modifier: Modifier
    var width: Int
    var height: Int
    var x: Int
    var y: Int

    fun renderTo(canvas: GuiyCanvas)
}

