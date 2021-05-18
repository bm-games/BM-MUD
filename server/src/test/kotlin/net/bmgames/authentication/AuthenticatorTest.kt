package net.bmgames.authentication

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.reflection.beLateInit
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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


class AuthenticatorTest : FunSpec({

    lateinit var authenticator: Authenticator
    lateinit var userHandler: UserHandler
    lateinit var authHelper: AuthHelper
    lateinit var mailNotifier: MailNotifier
    lateinit var userRepository: UserRepository


    beforeTest {
        authHelper = AuthHelper(DEMO_CONFIG)
        mailNotifier = MailNotifier(DEMO_CONFIG)
        userHandler = UserHandler(mailNotifier, authHelper)
        authenticator = Authenticator(authHelper, userHandler)
        userRepository = UserRepository

        mockkObject(userHandler)
        mockkObject(authHelper)
        mockkObject(userRepository)

        every { authHelper.hashPassword(pw) } returns "1234"
        every { userHandler.checkMailApproved(name) } returns true
        every { userRepository.getUserByMail(mail) } returns testuser
        every { userHandler.createUser(testuser) } returns true
    }

    test("Register should Fail") {
        every { userHandler.checkRegisterPossible(mail, name) } returns false
        "${authenticator.registerUser(mail, name, pw)}" shouldBe "Either.Left(${message("auth.user-exists")})"
    }
    test("Register should be successfull") {
        every { userHandler.checkRegisterPossible(mail, name) } returns true
        "${authenticator.registerUser(mail, name, pw)}" shouldBe "Either.Right(kotlin.Unit)"
    }

    test("Should Login successfully") {
        "${authenticator.loginUser(mail, pw)}" shouldBe "Either.Right($testuser)"
    }

    test("Should not Login because of Wrong Credentials") {
        every { userRepository.getUserByMail(mail) } returns null
        "${authenticator.loginUser(mail, pw)}" shouldBe "Either.Left(${message("auth.wrong-password-or-no-user")})"
    }



    test("Password Change should give Wrong Password Error") {
        "${authenticator.changePassword(testuser,"wrong","Dummy")}" shouldBe "Either.Left(${message("auth.incorrect-password")})"
    }



    afterSpec {
        unmockkAll()
    }

})

private infix fun <T, B> MockKStubScope<T, B>.returns(testuser: User): User {
    return testuser
}


