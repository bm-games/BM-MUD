package net.bmgames.authentication

import io.ktor.auth.*
import kotlinx.serialization.Serializable


/**
 * User Data class
 *
 * @property email Identifies user uniquely.
 * @property username Identifies user uniquely.
 * @property passwordHash Hashed user password.
 *
 * @constructor is also the primary constructor of the class
 *
 * */

@Serializable
data class User(
    val email: String,
    val username: String,
    val passwordHash: String,
    val registrationKey: String?,
    val id: Int? = null
): Principal

fun User.isMailVerified() = registrationKey == null
