package net.bmgames.game

import kotlinx.serialization.Serializable

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
