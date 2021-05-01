package net.bmgames.game.commands

import arrow.core.Either
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import net.bmgames.ErrorMessage
import net.bmgames.authentication.User
import net.bmgames.configurator.model.CommandConfig
import net.bmgames.game.commands.MoveCommand.Direction
import net.bmgames.game.state.Avatar
import net.bmgames.game.state.Player

class CommandParserTest : FunSpec({

    test("test move command") {
        val parser = CommandParser<Player.Normal>(CommandConfig())
        val player = Player.Normal(User("", "", "", true), Avatar("test"))

        val command: Either<ErrorMessage, Command<Player.Normal>> = parser.parse("move north")

        command.shouldBeRight()
        val moveCommand = command.value.shouldBeTypeOf<MoveCommand>()
        moveCommand.direction shouldBe Direction.NORTH
    }

})
