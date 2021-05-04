package net.bmgames.authentication

import kotlinx.serialization.Serializable


/**
 * User Data class
 *
 * @property email Identifies user uniquely.
 * @property username Identifies user uniquely.
 * @property passwordHash Hashed User Password.
 *
 * @constructor is also the primary constructor of the class
 *
 * */

@Serializable
data class User(
    val email: String,
    val username: String,
    val passwordHash: String,
    val registrationKey: String?
)

data class Login(
    val user: User,
    val jwttoken: String,
)