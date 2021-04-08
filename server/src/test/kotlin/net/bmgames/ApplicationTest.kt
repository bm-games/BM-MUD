package net.bmgames

import io.kotest.assertions.ktor.shouldHaveContent
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.ktor.http.*
import io.ktor.server.testing.*
import net.bmgames.plugins.configureRouting
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ApplicationTest : StringSpec({

    "Root url should display 'Hello World'" {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus HttpStatusCode.OK
                response shouldHaveContent "Hello World!"
            }
        }
    }

})
