package net.bmgames.database

import net.bmgames.authentication.PASSWORD_LENGTH
import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table - User
 * */
object UserTable : Table("User") {
    val username = varchar("username", NAME_LENGTH)
    val email = varchar("email", NAME_LENGTH)
    val passwordHash = varchar("passwordHash", PW_LENGTH)
    val registrationKey = varchar("registrationKey", REG_LENGTH).nullable()
    override val primaryKey = PrimaryKey(username, name = "username")

}
