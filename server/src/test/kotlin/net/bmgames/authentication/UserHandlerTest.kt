package net.bmgames.authentication

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.reflection.beLateInit
import io.kotest.matchers.shouldBe
import io.mockk.*
import net.bmgames.DEMO_CONFIG
import net.bmgames.TABLES
import net.bmgames.authentication.User
import net.bmgames.communication.MailNotifier
import net.bmgames.communication.Notifier
import net.bmgames.game.GAME_WITHOUT_PLAYER
import net.bmgames.game.GameEndpoint
import net.bmgames.game.GameManager
import net.bmgames.game.GameOverview.Permission.Yes
import net.bmgames.game.NOOP_NOTIFIER
import net.bmgames.message
import net.bmgames.state.GameRepository
import net.bmgames.state.UserRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


class UserHandlerTest : FunSpec({

    lateinit var authenticator: Authenticator
    lateinit var userHandler: UserHandler
    lateinit var authHelper: AuthHelper
    lateinit var mailNotifier: MailNotifier
    lateinit var userRepository: UserRepository


    beforeTest {
        authHelper = AuthHelper(DEMO_CONFIG)
        mailNotifier = MailNotifier(DEMO_CONFIG)
        userHandler = UserHandler(mailNotifier, authHelper)
    }

    test("create User should Fail") {
        userHandler.createUser(testuser) shouldBe false
    }




})



