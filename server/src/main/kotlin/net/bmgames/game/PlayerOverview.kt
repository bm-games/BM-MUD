package net.bmgames.game

import kotlinx.serialization.Serializable
import net.bmgames.state.model.Avatar

/**
 * DTO to give some details for a concrete player in a concrete game
 *
 * @property avatar The avatar of the player
 * @property room Name of the current room
 * */
@Serializable
data class PlayerOverview(
    val avatar: Avatar,
    val maxHealth: Int,
    val health: Int,
    val room: String
)
