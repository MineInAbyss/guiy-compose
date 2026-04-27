package com.mineinabyss.guiy.modifiers

import androidx.compose.runtime.Stable
import com.mineinabyss.guiy.draw.ContentDrawScope
import com.mineinabyss.guiy.draw.DrawScope
import com.mineinabyss.guiy.draw.InventoryCanvas
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun Modifier.drawBehind(
    onDraw: DrawScope.() -> Unit,
): Modifier = this then DrawWithContentElement(null) {
    onDraw(this)
    drawContent()
}

@Stable
fun Modifier.background(
    material: Material,
) =
    //TODO change to node to cache ItemStack instance
    drawBehind { drawItemRect(itemStack = ItemStack(material)) }

fun Modifier.drawWithContent(
    overrideCanvas: InventoryCanvas? = null,
    onDraw: ContentDrawScope.() -> Unit,
): Modifier = this then DrawWithContentElement(overrideCanvas, onDraw)


private data class DrawWithContentElement(
    val overrideCanvas: InventoryCanvas?,
    val onDraw: ContentDrawScope.() -> Unit,
) : ModifierNodeElement<DrawWithContentNode>() {
    override fun create(): DrawWithContentNode = DrawWithContentNode(overrideCanvas, onDraw)

    override fun update(node: DrawWithContentNode) {
        node.onDraw = onDraw
    }

    override fun toString() = "DrawWithContentNode"
}

private class DrawWithContentNode(
    override var overrideCanvas: InventoryCanvas?,
    var onDraw: ContentDrawScope.() -> Unit,
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        onDraw()
    }
}