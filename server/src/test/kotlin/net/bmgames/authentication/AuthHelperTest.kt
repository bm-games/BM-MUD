package net.bmgames.authentication

import io.kotest.core.spec.style.FunSpec
import io.kotest.property.forAll
import net.bmgames.DEMO_CONFIG

class AuthHelperTest : FunSpec({

    val authHelper = AuthHelper(DEMO_CONFIG)

    test("Any password should be the same after hashing and unhashing it") {
        forAll<String> { string ->
            authHelper.unhashPassword(authHelper.hashPassword(string)) == string
        }
    }

})
