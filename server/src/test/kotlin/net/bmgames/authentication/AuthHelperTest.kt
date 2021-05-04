package net.bmgames.authentication

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.bmgames.DEMO_CONFIG
import net.bmgames.game.GameManager

class AuthHelperTest : FunSpec({

    lateinit var authHelper: AuthHelper

    xtest("Password Gurkensalat should be the same after Hasing and Unhashing it") {
        val hash = authHelper.hashPassword("Gurkensalat")
        val test = authHelper.unhashPassword(hash)
        test shouldBe "Gurkensalat"
    }

})
