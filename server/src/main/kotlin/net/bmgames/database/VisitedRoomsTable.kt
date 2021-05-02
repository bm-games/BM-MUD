package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object VisitedRoomsTable : Table("VisitedRooms") {
    val id = integer("visitedRoomsId")
    val playerId = integer("playerId")
    val game = varchar("gameName", NAME_LENGTH)
    val roomId = integer("roomId")

    override val primaryKey = PrimaryKey(id, name = "visitedRoomsId")

}
