package net.bmgames.database

import net.bmgames.authentication.PASSWORD_LENGTH
import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object UserTable : Table("User") {
    val username = varchar("username", NAME_LENGTH)
    val email = varchar("email", NAME_LENGTH)
    val passwordHash = varchar("passwordHash", PASSWORD_LENGTH)
    val mailVerified = bool("mailVerified")

    override val primaryKey = PrimaryKey(username, name = "username")

}
