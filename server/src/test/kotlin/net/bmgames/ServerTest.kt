package net.bmgames

import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.ktor.sessions.*
import net.bmgames.authentication.User
import net.bmgames.game.PLAYER


class ServerTest : FunSpec({

    test("Root url should redirect to index.html") {
        withTestApplication({ installServer(DEMO_CONFIG) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus 302
            }
        }
    }

})


fun withAuthenticatedTestApplication(user: User, block: TestApplicationEngine.() -> Unit) = withTestApplication {
    application.installServer(DEMO_CONFIG)

    application.routing {
        get("/test") { call.sessions.set(user) }
    }
    cookiesSession {
        handleRequest(HttpMethod.Get, "/test")
        block()
    }
}
