package com.mineinabyss.guiy.modifiers.scroll

import androidx.compose.runtime.Composable
import com.mineinabyss.guiy.components.items.LocalItemTheme
import com.mineinabyss.guiy.interaction.ScrollEvent
import com.mineinabyss.guiy.modifiers.ItemTransformModifierNode
import com.mineinabyss.guiy.modifiers.ScrollModifierNode
import com.mineinabyss.guiy.modifiers.background
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement
import org.bukkit.inventory.ItemStack

data class ScrollModifierElement(
    val onScroll: ScrollEvent.() -> Unit,
) : ModifierNodeElement<ScrollableModifierNode>() {
    override fun create() = ScrollableModifierNode(onScroll)

    override fun update(node: ScrollableModifierNode) {
        node.onScroll = onScroll
    }
}

class ScrollableModifierNode(
    var onScroll: ScrollEvent.() -> Unit,
) : Modifier.Node(), ScrollModifierNode, ItemTransformModifierNode {
    override fun onScroll(event: ScrollEvent) {
        event.isConsumed = true
        onScroll.invoke(event)
    }

    override fun transform(itemStack: ItemStack) = BundleScrolling.asScrollable(itemStack)
}

/**
 * Listens to the scroll wheel while the cursor is over an item inside this composable's bounds, empty slots do not receive scrolls.
 * Items drawn inside are sent to the client as a different item type with the same components, so do not read them back from the inventory
 */
fun Modifier.onScroll(onScroll: ScrollEvent.() -> Unit) = then(ScrollModifierElement(onScroll))

// The scroll wheel only registers over an item, so empty slots get one without a model or tooltip
@Composable
fun Modifier.scrollableWhenEmpty(): Modifier = background(LocalItemTheme.current.hiddenTooltip)
