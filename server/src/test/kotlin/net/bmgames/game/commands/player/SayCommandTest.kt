package net.bmgames.game.commands.player

import arrow.core.right
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.MASTER
import net.bmgames.game.PLAYER
import net.bmgames.game.action.MessageAction
import net.bmgames.game.message.Message
import net.bmgames.game.message.Message.Chat
import net.bmgames.toList

class SayCommandTest : FunSpec({

    test("Online player should receive message") {
        val actions = SayCommand().apply { parse("Hi!".toList()) }
            .toAction(PLAYER, GAME_WITH_PLAYER)

        actions shouldBe MessageAction(MASTER, Chat(PLAYER.ingameName, "Hi!")).toList().right()
    }

    test("Offline player should not receive message") {
        val actions = WhisperCommand().apply { parse(listOf("Günther", "Hi!")) }
            .toAction(PLAYER, GAME_WITH_PLAYER)

        actions.shouldBeLeft()
    }

})
