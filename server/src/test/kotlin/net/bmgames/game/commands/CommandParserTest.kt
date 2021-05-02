package net.bmgames.game.commands

import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import net.bmgames.configurator.model.CommandConfig
import net.bmgames.game.commands.MoveCommand.Direction

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
        teleportCommand.target shouldBe "test"
        teleportCommand.newRoomID shouldBe "123"
    }

})
