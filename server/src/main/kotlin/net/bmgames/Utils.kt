package net.bmgames

import arrow.core.Either

typealias ErrorMessage = String

fun error(message: String) = Either.Left(message)
fun<T> success(result: T): Either<Nothing, T> = Either.Right(result)
val success = success(Unit)

