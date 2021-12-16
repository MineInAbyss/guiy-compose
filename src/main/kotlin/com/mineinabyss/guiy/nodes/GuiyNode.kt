package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.layout.MeasurePolicy
import com.mineinabyss.guiy.layout.Renderer
import com.mineinabyss.guiy.modifiers.Modifier

interface GuiyNode {
    var measurePolicy: MeasurePolicy
    var renderer: Renderer
    var modifier: Modifier
    var width: Int
    var height: Int
    var x: Int
    var y: Int

    fun renderTo(canvas: GuiyCanvas)

    companion object {
        val Constructor: () -> GuiyNode = ::LayoutNode
    }
}

