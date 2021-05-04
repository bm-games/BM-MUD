package net.bmgames.authentication

import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.serialization.Serializable
import net.bmgames.ErrorMessage


fun Route.installAuthEndpoint(authenticator: Authenticator) {

    route("/auth") {
        post("/login") {
            call.receive<Login>()
                .rightIfNotNull { "Login parameters not supplied" }
                .flatMap { (mail, password) -> authenticator.loginUser(mail, password) }
                .fold(
                    { error -> call.respond(HttpStatusCode.BadRequest, error) },
                    { user ->
                        call.sessions.set(user)
                        call.respond(HttpStatusCode.Accepted, mapOf("username" to user.username))
                    }
                )

        }
        post("/register") {
            call.receive<Register>()
                .rightIfNotNull { "Register parameters not supplied" }
                .flatMap { (mail, username, password) -> authenticator.registerUser(mail, username, password) }
                .fold(
                    { error -> call.respond(HttpStatusCode.BadRequest, error) },
                    { call.respond(HttpStatusCode.Accepted) }
                )

        }
        post("/reset") {
            call.receive<ResetPassword>()
                .rightIfNotNull { "Parameters not supplied" }
                .map { (mail) -> authenticator.resetPassword(mail) }
                .fold(
                    { error -> call.respond(HttpStatusCode.BadRequest, error) },
                    { call.respond(HttpStatusCode.Accepted) }
                )

        }
        get("/verify/{token}") {
            call.parameters["token"].rightIfNotNull { "Not valid token" }
                .flatMap { authenticator.verifyToken(it) }
                .fold(
                    { error -> call.respond(HttpStatusCode.BadRequest, error) },
                    { call.respondRedirect("/") }
                )
        }
        authenticate {
            post("/changePassword") {
                either<ErrorMessage, Unit> {
                    val user = call.getUser().rightIfNotNull { "User not signed in" }.bind()
                    call.receive<ChangePassword>()
                        .rightIfNotNull { "Parameters not supplied" }
                        .flatMap { (old, new) -> authenticator.changePassword(user, old, new) }
                        .bind()
                }.fold(
                    { error -> call.respond(HttpStatusCode.BadRequest, error) },
                    { call.respond(HttpStatusCode.Accepted) }
                )
            }

            get("/logout") {
                call.sessions.clear<User>()
                call.respondRedirect("/")
            }
        }
    }
}

@Serializable
private data class Login(val email: String, val password: String)

@Serializable
private data class Register(val email: String, val username: String, val password: String)

@Serializable
private data class ResetPassword(val email: String)

@Serializable
private data class ChangePassword(val oldPassword: String, val newPassword: String)
