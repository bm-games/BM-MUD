package net.bmgames

import arrow.core.Either
import arrow.core.Option
import arrow.core.computations.EitherEffect
import arrow.core.computations.RestrictedEitherEffect
import arrow.core.left
import arrow.core.right
import arrow.optics.Optional
import arrow.optics.POptional
import arrow.optics.dsl.at
import arrow.optics.dsl.index
import arrow.optics.typeclasses.At
import arrow.optics.typeclasses.Index
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

typealias ErrorMessage = String

/**
 * [Either.Left] of [message] ([ErrorMessage])
 * */
fun errorMsg(message: String) = Either.Left(message)


/**
 * [Either.Right] of [result] ([T])
 * */
fun <T> success(result: T): Either<Nothing, T> = Either.Right(result)

/**
 * [success] of [Unit]
 * */
val success = success(Unit)

/**
 * Converts [A] to [Either.Right] with [f] if not null.
 * Otherwise it is a [Either.Left] of [default]
 * */
inline fun <E, A, B> A?.toEither(default: () -> E, f: (A) -> B): Either<E, B> =
    if (this != null) {
        f(this).right()
    } else {
        default().left()
    }

fun <T> T.toList() = listOf(this)


/**
 * Guards an [EitherEffect] according to the condition.
 * @param condition If its true, the error is raised, otherwise nothing happens.
 * */
suspend fun <E, A> EitherEffect<E, A>.guard(condition: Boolean, error: () -> E) {
    if (condition) control().shift<A>(error().left())
}

/**
 * Responds the call successful if this is [Either.Right].
 * Answers with [HttpStatusCode.BadRequest] otherwise
 *
 * @param call The call to respond to
 * */
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

/**
 * Modify the focus of a [Optional] with a function f
 */
fun <S, A> Optional<S, A>.modify(f: (A) -> A): (S) -> S = { s -> modify(s, f) }

/**
 * Get the modified source of a [Optional]
 */
fun <S, A> Optional<S, A>.set(focus: A): (S) -> S = { set(it, focus) }

/**
 * DSL to compose [Index] with an [Optional] for a structure [Map] to focus in on [V] at given index [K]
 */
fun <T, K, V> Optional<T, Map<K, V>>.atIndex(key: K): Optional<T, V> = index(Index.map(), key)

/**
 * DSL to compose [At] with an [Optional] for a structure [Map] to focus in on [V] at given index [K]
 */
fun <T, K, V> Optional<T, Map<K, V>>.at(key: K): Optional<T, Option<V>> = at(At.map(), key)
