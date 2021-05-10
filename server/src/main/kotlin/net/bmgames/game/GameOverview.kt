package net.bmgames.game

import kotlinx.serialization.Serializable

@Serializable
data class GameOverview (
    val name: String,
//    val isStarted: Boolean,
    val onlinePlayers: Int,
    val masterOnline: Boolean,
    val avatarCount: Int,
    val userPermitted: Boolean
)
