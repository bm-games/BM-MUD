package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object PlayerTable : Table("Player") {
    val id = integer("playerID")
    val avatarId = integer("avatarId")
    val game = varchar("gameName", NAME_LENGTH)
    val user = varchar("userName", NAME_LENGTH)
    val roomId = integer("roomId")
    val health = integer("health")

    override val primaryKey = PrimaryKey(id, name = "playerID")

}
