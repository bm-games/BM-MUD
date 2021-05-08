package net.bmgames

import arrow.core.Either
import arrow.core.left
import arrow.core.right

typealias ErrorMessage = String

fun error(message: String) = Either.Left(message)
fun <T> success(result: T): Either<Nothing, T> = Either.Right(result)
val success = success(Unit)

inline fun <E, A, B> A?.toEither(default: () -> E, f: (A) -> B): Either<E, B> =
    if (this != null) {
        f(this).right()
    } else {
        default().left()
    }

fun <T> T.toList() = listOf(this)


fun Long.secondsRemaining(): Int = (this - System.currentTimeMillis()).toInt()
