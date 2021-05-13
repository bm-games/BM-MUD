package net.bmgames

import arrow.core.Either
import arrow.core.computations.EitherEffect
import arrow.core.left
import arrow.core.right
import arrow.optics.Lens
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

typealias ErrorMessage = String

fun errorMsg(message: String) = Either.Left(message)
fun <T> success(result: T): Either<Nothing, T> = Either.Right(result)
val success = success(Unit)

inline fun <E, A, B> A?.toEither(default: () -> E, f: (A) -> B): Either<E, B> =
    if (this != null) {
        f(this).right()
    } else {
        default().left()
    }

fun <T> T.toList() = listOf(this)

suspend fun <E, A> EitherEffect<E, A>.guard(condition: Boolean, error: () -> E) {
    if (condition) control().shift<A>(error().left())
}

suspend inline fun Either<ErrorMessage, Unit>.acceptOrReject(call: ApplicationCall) =
    fold(
        { error -> call.respond(HttpStatusCode.BadRequest, error) },
        { call.respond(HttpStatusCode.Accepted) }
    )



fun Long.secondsRemaining(): Int = (this - System.currentTimeMillis()).toInt()

fun Float.toRelativePercent() = (this - 1) * 100
fun <S, A> Lens<S, A>.modify(f: (A) -> A): (S) -> S = { s -> modify(s, f) }

fun <S, A> Lens<S, A>.set(a: A): (S) -> S = { s -> set(s, a) }
