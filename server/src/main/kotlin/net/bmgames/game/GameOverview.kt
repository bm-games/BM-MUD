package net.bmgames.game

import kotlinx.serialization.Serializable

/**
 * DTO for an overview for a concrete game from a user perspective.
 *
 * @property description Textual overview of the game
 * @property isMaster If the user is the master. Used for deleting the MUD
 * */
@Serializable
data class GameOverview (
    val name: String,
    val description: String,
    val isMaster: Boolean,
    val onlinePlayers: Int,
    val masterOnline: Boolean,
    val avatarCount: Int,
    val userPermitted: Permission
){
    enum class Permission {Yes, No, Pending}
}
