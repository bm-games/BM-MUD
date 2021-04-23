package net.bmgames.authentication

/**
 * @param email Identifies user uniquely.
 * @param username Identifies user uniquely.
 * @param passwordHash Hashed User Password.
 * @param mailVerified Boolean value which determines whether a user has confirmed his e-mail or not.
 * @param registrationKey Is only assigned when User maiLVerified is not true and value is used to verify this user.
 * */
data class User(
    val email: String,
    val username: String,
    val passwordHash: String,
    val mailVerified: Boolean,
    var registrationKey: String = "",
)