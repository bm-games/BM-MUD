package net.bmgames

import arrow.core.Either

typealias ErrorMessage = String

val success = Either.Right(Unit)
fun error(message: String) = Either.Left(message)
