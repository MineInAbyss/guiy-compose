package com.mineinabyss.guiy.components.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.destroystokyo.paper.profile.PlayerProfile
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.items.PlayerHeadType.*
import com.mineinabyss.guiy.components.rememberMiniMsg
import com.mineinabyss.idofront.textcomponents.miniMsg
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.ItemLore
import io.papermc.paper.datacomponent.item.ResolvableProfile
import me.dvyy.compose.mini.modifier.Modifier
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack

enum class PlayerHeadType {
    NORMAL,
    FLAT,
    LARGE,
    NORMAL_CENTER,
    LARGE_CENTER,
}

@Composable
fun PlayerHead(
    player: OfflinePlayer,
    title: String? = null,
    vararg lore: String,
    type: PlayerHeadType = NORMAL,
    modifier: Modifier = Modifier,
) {
    val profile = rememberPlayerProfile(player)
    val title = remember(title) { title?.miniMsg() }
    val lore = rememberMiniMsg(*lore)
    val item = remember(profile, title, lore) {
        ItemStack.of(Material.PLAYER_HEAD).apply {
            if (profile != null) setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(profile))
            if (title != null) setData(DataComponentTypes.ITEM_NAME, title)
            setData(DataComponentTypes.LORE, ItemLore.lore(lore.toList()))
            val modelData = when (type) {
                NORMAL -> null
                FLAT -> 1f //TODO customize via CompositionLocal
                LARGE -> 2f
                NORMAL_CENTER -> 3f
                LARGE_CENTER -> 4f
            }
            if (modelData != null) setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(modelData))
        }
    }
    Item(item, modifier)
}

@Composable
fun rememberPlayerProfile(player: OfflinePlayer): PlayerProfile? {
    return produceState<PlayerProfile?>(null, player) {
        value = PlayerProfileCache.get(player)
    }.value
}
