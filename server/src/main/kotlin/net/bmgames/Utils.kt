package net.bmgames

import arrow.core.Either
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.sessions.*
import net.bmgames.authentication.User

typealias ErrorMessage = String

fun error(message: String) = Either.Left(message)
fun <T> success(result: T): Either<Nothing, T> = Either.Right(result)
val success = success(Unit)


