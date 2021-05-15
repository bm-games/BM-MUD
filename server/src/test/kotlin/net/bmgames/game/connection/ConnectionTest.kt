package net.bmgames.game.connection

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.launch
import net.bmgames.errorMsg
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.player.MoveCommand
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player
import net.bmgames.success

class ConnectionTest : FunSpec({

    val connection = Connection { command ->
        if (command == "move") {
            success(MoveCommand() as Command<Player>)
        } else {
            errorMsg("fail")
        }
    }

    test("Correct command should be sent") {
        launch {
            val result = connection.tryQueueCommand("move")
            result.shouldBeRight()
        }

        val command = connection.incoming.receive()
        command.shouldBeTypeOf<MoveCommand>()
    }

    test("Wrong command should not be sent") {
        val result = connection.tryQueueCommand("aaaaaaaaaaaa")
        result.shouldBeLeft()
    }

    test("Message should be sent") {
        val msg = Message.Text("hi")
        launch { connection.outgoingChannel.send(msg)}

        connection.outgoing.receive() shouldBe msg
    }

})
