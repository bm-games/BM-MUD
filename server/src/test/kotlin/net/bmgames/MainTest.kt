package net.bmgames

import io.kotest.assertions.ktor.shouldHaveContent
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.http.*
import io.ktor.server.testing.*


class MainTest : FunSpec({

    test("Root url should redirect to index.html" ){
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus 302
            }
        }
    }

})
