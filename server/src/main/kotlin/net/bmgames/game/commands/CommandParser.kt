package net.bmgames.game.commands

import arrow.core.Either
import net.bmgames.ErrorMessage
import net.bmgames.configurator.CommandConfig
import net.bmgames.error
import net.bmgames.game.state.Player

class CommandParser(
    val commandConfig: CommandConfig
) {
    fun parse(player: Player, command: String): Either<ErrorMessage, Command<Player>> {
        return when (player) {
            is Player.Master -> error("TODO: Parse master command")
            is Player.Normal -> error("TODO: Parse player command")
        }
    }
}
