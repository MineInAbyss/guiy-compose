package com.mineinabyss.guiy.layout

import me.dvyy.compose.mini.layout.ComposeMiniNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.NodeChain
import me.dvyy.compose.mini.modifier.WrappingChain

/**
 * TODO structure is really not decided on yet.
 *  I'd really like to avoid inheritance, and have only one ComposableNode call that creates Layout.
 *  You can configure stuff through [measurePolicy], [placer], and the [modifier], but things creates some problems
 *  when trying to make your own composable nodes that interact with this Layout node.
 */
internal class GuiyNode() : ComposeMiniNode() {
    var parent: GuiyNode? = null
    val children = mutableListOf<GuiyNode>()
    val layoutDelegate get() = wrappedChain.head

    private val nodeChain = NodeChain()
    private val wrappedChain = WrappingChain(nodeChain) { GuiyModifierNode(it, this) }

    override fun setModifier(modifier: Modifier) {
        nodeChain.updateFrom(modifier)
        wrappedChain.update()
    }

    override fun toString() = children.joinToString(prefix = "LayoutNode(", postfix = ")")

    internal companion object {
        val Constructor: () -> GuiyNode = ::GuiyNode
    }
}

