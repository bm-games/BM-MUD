package net.bmgames.database

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable


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

