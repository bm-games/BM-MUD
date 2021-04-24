package net.bmgames.authentication

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


/**
 * Represents the Database Table
 * */
object usertable : Table() {
    val email = varchar("email", 100)
    val username = varchar("username", 100)
    val passwordHash = varchar("passwordHash", 100)
    val mailVerified = bool("mailVerified")

    override val primaryKey = PrimaryKey(username, name = "username")

    var currentUser : User? = null
}

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
