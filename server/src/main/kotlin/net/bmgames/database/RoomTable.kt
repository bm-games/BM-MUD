package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RoomTable : Table("Room") {
    val id = integer("roomId")
    val game = varchar("gameName", NAME_LENGTH)
    val configId = integer("configId")

    override val primaryKey = PrimaryKey(id, name = "roomId")

}
