package com.mineinabyss.guiy.nodes

import com.mineinabyss.guiy.canvas.GuiyCanvas
import com.mineinabyss.guiy.layout.LayoutNode
import com.mineinabyss.guiy.layout.MeasurePolicy
import com.mineinabyss.guiy.layout.Renderer
import com.mineinabyss.guiy.modifiers.Modifier

interface GuiyNode {
    var measurePolicy: MeasurePolicy
    var modifier: Modifier
    var renderer: Renderer
    var canvas: GuiyCanvas?
    var width: Int
    var height: Int
    var x: Int
    var y: Int

    fun render() = renderTo(null)
    fun renderTo(guiyCanvas: GuiyCanvas?)

    companion object {
        val Constructor: () -> GuiyNode = ::LayoutNode
    }
}

