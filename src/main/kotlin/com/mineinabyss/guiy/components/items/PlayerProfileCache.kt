package com.mineinabyss.guiy.components.items

import com.destroystokyo.paper.profile.PlayerProfile
import kotlinx.coroutines.future.asDeferred
import org.bukkit.OfflinePlayer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object PlayerProfileCache {
    internal val profileCache: MutableMap<UUID, PlayerProfile> = ConcurrentHashMap()

    suspend fun get(player: OfflinePlayer): PlayerProfile = when {
        player.isOnline -> {
            val profile = player.playerProfile
            profileCache.putIfAbsent(player.uniqueId, profile)
            profile
        }

        player.uniqueId in profileCache -> profileCache[player.uniqueId]!!

        else -> {
            val profile = player.playerProfile.update().asDeferred().await()
            profileCache[player.uniqueId] = profile
            profile
        }
    }
}
