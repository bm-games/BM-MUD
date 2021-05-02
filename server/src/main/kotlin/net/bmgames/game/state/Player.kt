package net.bmgames.game.state

import kotlinx.serialization.Serializable
import net.bmgames.authentication.User

@Serializable
sealed class Player {
    abstract val user: User
    abstract val ingameName: String

    @Serializable
    class Master(override val user: User) : Player() {
        override val ingameName = user.username
    }

    @Serializable
    class Normal(override val user: User, val avatar: Avatar) : Player() {
        override val ingameName = avatar.name
    }
}
