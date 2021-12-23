package com.mineinabyss.guiy.components.canvases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.inventory.GuiyCanvas
import com.mineinabyss.guiy.inventory.GuiyOwner
import com.mineinabyss.guiy.inventory.LocalCanvas
import com.mineinabyss.guiy.layout.Layout
import com.mineinabyss.guiy.layout.Size
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.SizeModifier
import com.mineinabyss.guiy.modifiers.sizeIn
import com.mineinabyss.guiy.nodes.InventoryCloseScope
import com.mineinabyss.guiy.nodes.StaticMeasurePolicy
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor.GREEN
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.lang.Integer.max

const val CHEST_WIDTH = 9
const val MIN_CHEST_HEIGHT = 1
const val MAX_CHEST_HEIGHT = 6

@Composable
fun GuiyOwner.Chest(
    viewers: Set<Player>,
    title: String,
    modifier: Modifier = Modifier,
    onClose: (InventoryCloseScope.(player: Player) -> Unit) = {},
    content: @Composable () -> Unit,
) {
    val constrainedModifier = modifier.sizeIn(CHEST_WIDTH, CHEST_WIDTH, MIN_CHEST_HEIGHT, MAX_CHEST_HEIGHT)
    //TODO a proper way of reading size (onSizeChange recomposes twice when both title and size change.)
    val size = remember(constrainedModifier) {
        constrainedModifier.foldOut(Size()) { element, acc ->
            if (element is SizeModifier)
                acc.copy(
                    width = max(element.constraints.minWidth, acc.width),
                    height = max(element.constraints.minHeight, acc.height)
                )
            else acc
        }
    }

    val canvas = remember(size) {
        object : GuiyCanvas {
            override fun set(inventory: Inventory, x: Int, y: Int, item: ItemStack?) {
                if (/*!updating && */x in 0 until size.width && y in 0 until size.height) {
                    val slot = y * size.width + x
                    if (slot < inventory.size)
                        inventory.setItem(slot, item)
                }
            }
        }
    }

    val holder = rememberInventoryHolder(viewers, onClose)

    // Create new inventory when any appropriate value changes
    val inventory: Inventory = remember(title, size) {
        if (size == Size()) return@remember null
        Bukkit.createInventory(holder, CHEST_WIDTH * size.height, Component.text(title)).also {
            println("${GREEN}Updated inventory to $it, ($CHEST_WIDTH, ${size.height})")
            holder.activeInventory = it
        }
    } ?: run {
        Layout(
            measurePolicy = StaticMeasurePolicy,
            modifier = constrainedModifier
        )
        return
    }

    CompositionLocalProvider(LocalCanvas provides canvas) {
        Inventory(
            inventory = inventory,
            viewers = viewers,
            modifier = constrainedModifier
        ) {
            println("Rendering children with inventory $inventory")
            content()
        }
    }
}
