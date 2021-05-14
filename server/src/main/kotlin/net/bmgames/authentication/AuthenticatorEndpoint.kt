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
import net.bmgames.acceptOrReject
import net.bmgames.message


fun Route.installAuthEndpoint(authenticator: Authenticator) {

    route("/auth") {
        post("/login") {
            call.receive<Login>()
                .rightIfNotNull {message("auth.login-parameters")}
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
                .rightIfNotNull {message("auth.register-parameters")}
                .flatMap { (mail, username, password) -> authenticator.registerUser(mail, username, password) }
                .acceptOrReject(call)

        }
        post("/reset") {
            call.receive<ResetPassword>()
                .rightIfNotNull {message("auth.parameters")}
                .map { (mail) -> authenticator.resetPassword(mail) }
                .acceptOrReject(call)

        }
        get("/verify/{token}") {
            call.parameters["token"].rightIfNotNull {message("auth.token")}
                .flatMap { authenticator.verifyToken(it) }
                .fold(
                    { error -> call.respond(HttpStatusCode.BadRequest, error) },
                    { call.respondRedirect("/") }
                )
        }
        authenticate {
            post("/changePassword") {
                either<ErrorMessage, Unit> {
                    val user = call.getUser().rightIfNotNull {message("auth.not-signed-in")}.bind()
                    call.receive<ChangePassword>()
                        .rightIfNotNull {message("auth.parameters")}
                        .flatMap { (old, new) -> authenticator.changePassword(user, old, new) }
                        .bind()
                }.acceptOrReject(call)
            }

            get("/logout") {
                call.sessions.clear<User>()
                call.respond(HttpStatusCode.Accepted)
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
