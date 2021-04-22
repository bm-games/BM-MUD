package net.bmgames.auth


import com.auth0.jwt.JWT
import io.ktor.auth.Principal

data class User(val name: String, val email: String, val passwordHash: String) : Principal

interface Authenticator {
    fun registerUser(name: String, email: String, passwordHash: String): JWT
    fun getUser(token: JWT): User?
}

class UserDatabase {
    fun getUserByName(name: String): User? = TODO()
    fun saveUser(user: User): Unit = TODO()
}
