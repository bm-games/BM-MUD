package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RoomItemTable : Table("RoomItem") {
    val id = integer("roomItemId")
    val roomId = integer("roomId")
    val itemConfigId = integer("itemConfigId")

    override val primaryKey = PrimaryKey(id, name = "roomItemId")

}
