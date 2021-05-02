package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table - Verification
 * */
object VerificationTable : Table("Verification") {
    val registrationKey = varchar("registrationKey", REG_LENGTH)
    val username = varchar("username", NAME_LENGTH)
    val mailVerified = bool("mailVerified")
    override val primaryKey = PrimaryKey(registrationKey, name = "registrationKey")
}
