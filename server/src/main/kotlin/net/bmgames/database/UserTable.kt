package net.bmgames.database

import net.bmgames.authentication.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table - User
 * */
object UserTable : IntIdTable("User") {
    val username = varchar("username", NAME_LENGTH)
    val email = varchar("email", NAME_LENGTH)
    val passwordHash = varchar("passwordHash", PW_LENGTH)
    val registrationKey = varchar("registrationKey", REG_LENGTH).nullable()
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var username by UserTable.username

    var email by UserTable.email
    var passwordHash by UserTable.passwordHash
    var registrationKey by UserTable.registrationKey

    fun toUser(): User = User(email, username, passwordHash, registrationKey, id.value)
}

