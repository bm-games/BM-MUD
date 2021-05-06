package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable
import net.bmgames.authentication.User

@Serializable
@optics
sealed class Player {
    abstract val user: User
    abstract val ingameName: String

    @Serializable
    @optics
    data class Master(override val user: User) : Player() {
        override val ingameName = user.username
    }

    @Serializable
    @optics
    data class Normal(
        override val user: User,
        val avatar: Avatar,

        val inventory: Inventory,
        val room: String,

        val healthPoints: Int,
        val lastHit: Long?,
        val visitedRooms: Set<Int>
    ) : Player() {
        override val ingameName = avatar.name
    }
}
