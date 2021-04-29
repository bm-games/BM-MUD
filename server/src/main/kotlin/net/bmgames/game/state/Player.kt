package net.bmgames.game.state

import net.bmgames.authentication.User

sealed class Player(val user: User, val ingameName: String) {

    class Master(user: User) : Player(user, user.username)

    class Normal(user: User, val avatar: Avatar) : Player(user, avatar.name)
}
