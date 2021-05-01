package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object AvatarTable : Table("Avatar") {
    val id = integer("avatarID")
    val name = varchar("avatarName", NAME_LENGTH)
    val raceId = integer("raceID")
    val classId = integer("classID")

    override val primaryKey = PrimaryKey(id, name = "avatarId")

}
