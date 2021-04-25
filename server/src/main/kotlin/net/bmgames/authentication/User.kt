package net.bmgames.authentication


/**
 * User Data class
 *
 * @property email Identifies user uniquely.
 * @property username Identifies user uniquely.
 * @property passwordHash Hashed User Password.
 * @property mailVerified Boolean value which determines whether a user has confirmed his e-mail or not.
 * @property registrationKey Is only assigned when User maiLVerified is not true and value is used to verify this user.
 *
 * @constructor is also the primary constructor of the class
 *
 * */
data class User(
    val email: String,
    val username: String,
    val passwordHash: String,
    val mailVerified: Boolean,
    var registrationKey: String = ""
)
