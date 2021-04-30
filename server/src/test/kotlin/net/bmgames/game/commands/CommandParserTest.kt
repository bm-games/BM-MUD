package net.bmgames.game.commands

import arrow.core.getOrElse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import net.bmgames.authentication.User
import net.bmgames.configurator.CommandConfig
import net.bmgames.game.commands.MoveCommand.Direction
import net.bmgames.game.state.Avatar
import net.bmgames.game.state.Player

class CommandParserTest : FunSpec({

    test("test move command") {
        val parser = CommandParser(CommandConfig())
        val player = Player.Normal(User("", "", "", true), Avatar("test"))

        val command = parser.parse(player, "move north").getOrElse { null }

        command.shouldBeTypeOf<MoveCommand>()
        command.direction shouldBe Direction.NORTH
    }

})
