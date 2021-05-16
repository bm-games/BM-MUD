package net.bmgames.game.action

import arrow.core.Either
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.channels.shouldReceiveNoElementsWithin
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import net.bmgames.authentication.User
import net.bmgames.communication.Notifier
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.GameScope
import net.bmgames.game.NOOP_NOTIFIER
import net.bmgames.game.PLAYER
import net.bmgames.game.connection.Connection
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.connection.IConnection
import net.bmgames.game.message.Message
import java.time.Duration

class MessageActionTest : FunSpec({
    lateinit var connection: IConnection
    val runner = GameRunner(GAME_WITH_PLAYER.copy(onlinePlayers = emptyMap()), NOOP_NOTIFIER)
        .also {
            GameScope.launch {
                connection = (it.connect(PLAYER) as Either.Right).value
                connection.outgoing.receive() //Receive welcome message
            }
        }

    test("Should send message to online player") {
        val message = Message.Text("Hi")
        launch { MessageAction(PLAYER, message).run(runner) }
        connection.outgoing.receive() shouldBe message
    }
})
