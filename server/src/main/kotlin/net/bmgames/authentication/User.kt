package net.bmgames.authentication


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
data class User(
    val email: String,
    val username: String,
    val passwordHash: String,
    //val mailVerified: Boolean,
    //var registrationKey: String = ""
)
//wird aktuell nicht wirklich benötigt -> ggf. löschen
/*
data class Verification(
    var registrationKey: String = "",
    val username: String,
    val mailVerified: Boolean
)*/

data class Login(
    val user: User,
    val jwttoken: String,
)