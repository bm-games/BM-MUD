package net.bmgames.database

import net.bmgames.authentication.PASSWORD_LENGTH
import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table - User
 * */
object UserTable : IdTable<String>("User") {
    override val id = varchar("username", NAME_LENGTH).entityId()
    val email = varchar("email", NAME_LENGTH)
    val passwordHash = varchar("passwordHash", PW_LENGTH)
    val registrationKey = varchar("registrationKey", REG_LENGTH).nullable()
}

class UserDAO(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, UserDAO>(UserTable)

    val email by UserTable.email
    val passwordHash by UserTable.passwordHash
    val registrationKey by UserTable.registrationKey
}

