package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RoomConfigTable : Table("RoomConfig") {
    val id = integer("roomConfigId")
    val game = varchar("gameName", NAME_LENGTH)
    val name = varchar("roomName", NAME_LENGTH)
    val northId = integer("northId")
    val eastId = integer("eastId")
    val westId = integer("westId")
    val southId = integer("southId")
    val message = varchar("message", NAME_LENGTH)

    override val primaryKey = PrimaryKey(id, name = "roomConfigId")

}
