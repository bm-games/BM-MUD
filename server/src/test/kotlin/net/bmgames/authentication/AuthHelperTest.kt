package net.bmgames.authentication

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.forAll
import net.bmgames.DEMO_CONFIG
import org.junit.Test

class AuthHelperTest : FunSpec({

    val authHelper = AuthHelper(DEMO_CONFIG)

    test("Any password should be the same after hashing and unhashing it") {
        forAll<String> { string ->
            authHelper.unhashPassword(authHelper.hashPassword(string)) == string
        }
    }
    test("VerificationToken should be not null") {
        authHelper.generateVerificationToken() shouldNotBe null
    }
    test("Generated Password should be not null") {
        authHelper.generatePassword() shouldNotBe null
    }
    test("Generated Token should not be null") {
        authHelper.makeToken(testuser) shouldNotBe null
    }


})
