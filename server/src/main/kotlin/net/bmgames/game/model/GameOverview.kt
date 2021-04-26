package net.bmgames.game.model

import kotlinx.serialization.Serializable
import net.bmgames.configurator.model.DungeonConfig

@Serializable
data class GameOverview (
    val config: DungeonConfig,
    val onlinePlayers: Int,
    val masterOnline: Boolean,
    val avatarCount: Int,
    val userPermitted: Boolean
)
