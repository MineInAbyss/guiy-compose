package com.mineinabyss.guiy.modifiers.scroll

import com.mineinabyss.guiy.canvas.inventory.GuiyInventoryHolder
import com.mineinabyss.guiy.interaction.ScrollEvent
import com.mineinabyss.idofront.nms.networkPayload
import com.mineinabyss.idofront.nms.tags
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import it.unimi.dsi.fastutil.ints.IntArrayList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket
import net.minecraft.tags.ItemTags
import org.bukkit.Material
import org.bukkit.craftbukkit.util.CraftMagicNumbers
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.max

/**
 * Scrolling over a slot only reaches the server as the bundle select packet, which the client sends while hovering an item in `#minecraft:bundles` that has bundle contents.
 * Real bundles always draw a fullness bar, so items are carried by [CARRIER] instead, added to the tag only in the packet sent to clients since the server uses it for the bundle dye recipes
 */
internal object BundleScrolling {
    private val CARRIER = Material.PAPER

    // The client lets the selection cycle through at most 12 items
    private const val CONTENTS_SIZE = 12
    private val contents by lazy { BundleContents.bundleContents(List(CONTENTS_SIZE) { ItemStack.of(Material.STONE) }) }

    private class SlotState(val index: Int, val item: ItemStack?)

    private val slotStates = mutableMapOf<UUID, MutableMap<Int, SlotState>>()

    fun asScrollable(itemStack: ItemStack): ItemStack {
        if (itemStack.isEmpty || itemStack.hasData(DataComponentTypes.BUNDLE_CONTENTS)) return itemStack
        return ItemStack.of(CARRIER).apply {
            copyDataFrom(itemStack) { true }
            setData(DataComponentTypes.BUNDLE_CONTENTS, contents)
            amount = itemStack.amount
        }
    }

    fun withCarrierInBundleTag(packet: ClientboundUpdateTagsPacket): ClientboundUpdateTagsPacket {
        val itemTags = packet.tags()[Registries.ITEM]?.tags() ?: return packet
        val carrierId = BuiltInRegistries.ITEM.getId(CraftMagicNumbers.getItem(CARRIER))
        val bundles = IntArrayList(itemTags[ItemTags.BUNDLES.location()] ?: IntArrayList())
        if (carrierId in bundles) return packet

        bundles += carrierId
        itemTags[ItemTags.BUNDLES.location()] = bundles

        return ClientboundUpdateTagsPacket(packet.tags() + (Registries.ITEM to itemTags.networkPayload()))
    }

    /** Must run on the main thread */
    fun onSelect(player: Player, slot: Int, index: Int) {
        val inventory = player.openInventory.topInventory
        val holder = inventory.holder as? GuiyInventoryHolder ?: return
        if (slot !in 0 until inventory.size) return

        val item = inventory.getItem(slot)
        val previous = slotStates.getOrPut(player.uniqueId) { mutableMapOf() }.put(slot, SlotState(index, item))
        // The client sends -1 when the cursor leaves the slot
        if (index == -1) return

        // The client restarts at -1 whenever the slot gets a new stack, but packets sent before it arrived still continue from the old index
        val delta = when {
            previous == null -> step(-1, index)
            previous.item != item -> step(-1, index) ?: step(previous.index, index)
            else -> step(previous.index, index) ?: step(-1, index)
        } ?: return
        holder.onScroll(ScrollEvent.fromSlot(delta, slot, player))
    }

    fun reset(player: Player) {
        slotStates.remove(player.uniqueId)
    }

    private fun step(from: Int, to: Int) = when (to) {
        next(from, 1) -> 1
        next(from, -1) -> -1
        else -> null
    }

    // Matches ScrollWheelHandler.getNextScrollWheelSelection on the client
    private fun next(from: Int, direction: Int): Int {
        val selected = max(-1, from + direction)
        return if (selected < 0) selected + CONTENTS_SIZE else selected % CONTENTS_SIZE
    }
}
