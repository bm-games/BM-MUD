package net.bmgames

import arrow.core.Either
import arrow.core.Option
import arrow.core.computations.EitherEffect
import arrow.core.left
import arrow.core.right
import arrow.optics.Optional
import arrow.optics.dsl.at
import arrow.optics.dsl.index
import arrow.optics.typeclasses.At
import arrow.optics.typeclasses.Index
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

suspend inline fun <reified T> Either<ErrorMessage, T>.acceptOrReject(call: ApplicationCall) =
    fold(
        { error -> call.respond(HttpStatusCode.BadRequest, error) },
        { result ->
            if (result == null) call.respond(HttpStatusCode.Accepted) else call.respond(
                HttpStatusCode.Accepted,
                result
            )
        }
    )


fun Long.secondsRemaining(): Int = (this - System.currentTimeMillis()).toInt()
fun Float.toRelativePercent() = (this - 1) * 100

fun <S, A> Optional<S, A>.modify(f: (A) -> A): (S) -> S = { s -> modify(s, f) }
fun <S, A> Optional<S, A>.set(focus: A): (S) -> S = { set(it, focus) }
fun <T, K, V> Optional<T, Map<K, V>>.atIndex(key: K): Optional<T, V> = index(Index.map(), key)
fun <T, K, V> Optional<T, Map<K, V>>.at(key: K): Optional<T, Option<V>> = at(At.map(), key)
