package com.mineinabyss.guiy.components.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.destroystokyo.paper.profile.PlayerProfile
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.items.PlayerHeadType.*
import com.mineinabyss.guiy.components.rememberMiniMsg
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.textcomponents.miniMsg
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

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
        ItemStack(Material.PLAYER_HEAD).editItemMeta<SkullMeta> {
            if (playerProfile != null) this.playerProfile = playerProfile
            itemName(title)
            lore(lore.toList())
            when (type) {
                NORMAL -> {}
                FLAT -> setCustomModelData(1) //TODO customize via CompositionLocal
                LARGE -> setCustomModelData(2)
                NORMAL_CENTER -> setCustomModelData(3)
                LARGE_CENTER -> setCustomModelData(4)
            }
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
