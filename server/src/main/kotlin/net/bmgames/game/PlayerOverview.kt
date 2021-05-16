package net.bmgames.game

import kotlinx.serialization.Serializable
import net.bmgames.state.model.Avatar

@Serializable
data class PlayerOverview(
    val avatar: Avatar,
    val maxHealth: Int,
    val health: Int,
    val room: String
)
