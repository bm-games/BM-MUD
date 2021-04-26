package net.bmgames.authentication

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AuthHelperTest : FunSpec({
    test("Hashed password should be: Ux1sgq1OagEn5G77TLpUCA== ") {
        val test = AuthHelper.hashPassword("password")
        test shouldBe "Ux1sgq1OagEn5G77TLpUCA=="
    }

    test("Unhashed password should be: Gurkensalat") {
        val test = AuthHelper.unhashPassword("ohtBU7hSCvmQqZGbIkoFKg==")
        test shouldBe "Gurkensalat"
    }

})
