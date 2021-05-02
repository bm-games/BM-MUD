package net.bmgames.game

import net.bmgames.authentication.User
import net.bmgames.configurator.model.CommandConfig
import net.bmgames.configurator.model.DungeonConfig
import net.bmgames.game.state.Avatar
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player

val master = Player.Master(
    User("master", "master@master.de", "1234", true)
)
val PLAYER = Player.Normal(
    User("player1", "", "", true),
    Avatar("georkina")
)

val config = DungeonConfig("Dummy", CommandConfig())
val GAME_WITHOUT_PLAYER = Game(
    config = config,
    master = master,
    users = mapOf(master.ingameName to emptyList()),
    onlinePlayers = mapOf(master.ingameName to master)
)
val GAME_WITH_PLAYER = GAME_WITHOUT_PLAYER.copy(
    config = config.copy(name = "Test"),
    users = GAME_WITHOUT_PLAYER.users.plus(PLAYER.user.username to listOf(PLAYER.ingameName)),
    onlinePlayers = mapOf(PLAYER.ingameName to PLAYER)
)
