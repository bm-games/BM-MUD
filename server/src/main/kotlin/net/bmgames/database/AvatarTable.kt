package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object AvatarTable : Table("Avatar") {
    val id = integer("avatarId")
    val name = varchar("avatarName", NAME_LENGTH)
    val raceId = integer("raceId")
    val classId = integer("classId")

    override val primaryKey = PrimaryKey(id, name = "avatarId")

}
