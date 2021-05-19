package net.bmgames.game.commands

import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import net.bmgames.game.commands.master.TeleportCommand
import net.bmgames.game.commands.player.MoveCommand
import net.bmgames.state.model.CommandConfig
import net.bmgames.state.model.Direction

class CommandParserTest : FunSpec({

    test("test move command") {
        val parser = CommandParser(CommandConfig())

        val command = parser.parsePlayerCommand("move north")

        command.shouldBeRight()
        val moveCommand = command.value.shouldBeTypeOf<MoveCommand>()
        moveCommand.direction shouldBe Direction.NORTH
    }

    test("test teleport command") {
        val parser = CommandParser(CommandConfig())

        val command = parser.parseMasterCommand("teleport test 123")
        command.shouldBeRight()
        val teleportCommand = command.value.shouldBeTypeOf<TeleportCommand>()
        teleportCommand.playerName shouldBe "test"
        teleportCommand.destination shouldBe "123"
    }

})
