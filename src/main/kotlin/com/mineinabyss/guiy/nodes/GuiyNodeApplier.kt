package com.mineinabyss.guiy.nodes

import androidx.compose.runtime.AbstractApplier

internal class GuiyNodeApplier(root: GuiyNode) : AbstractApplier<GuiyNode>(root) {
    override fun insertTopDown(index: Int, instance: GuiyNode) {
        // Ignored, we insert bottom-up.
    }

    override fun insertBottomUp(index: Int, instance: GuiyNode) {
        val boxNode = current as BoxNode
        boxNode.children.add(index, instance)
    }

    override fun remove(index: Int, count: Int) {
        val boxNode = current as BoxNode
        boxNode.children.remove(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        val boxNode = current as BoxNode
        boxNode.children.move(from, to, count)
    }

    override fun onClear() {
        val boxNode = root as BoxNode
        boxNode.children.clear()
    }
}
