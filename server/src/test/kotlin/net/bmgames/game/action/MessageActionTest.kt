package net.bmgames.game.action

import arrow.core.Either
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.assertions.retry
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.channels.shouldReceiveNoElementsWithin
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.time.seconds

@OptIn(ExperimentalCoroutinesApi::class, kotlin.time.ExperimentalTime::class)
class MessageActionTest : FunSpec({
    lateinit var connection: IConnection
    val runner = GameRunner(GAME_WITH_PLAYER.copy(onlinePlayers = emptyMap()), NOOP_NOTIFIER)
        .also {
            GameScope.launch {
                connection = (it.connect(PLAYER) as Either.Right).value
            }
        }

    test("Should send message to online player") {
        val message = Message.Text("Hi")
        launch { MessageAction(PLAYER, message).run(runner) }
        retry(4, 10.seconds) {
            connection.outgoing.receive() shouldBe message
        }
    }
})
