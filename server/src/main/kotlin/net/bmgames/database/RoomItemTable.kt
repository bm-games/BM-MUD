package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RoomItemTable : Table("RoomItem") {
    val roomId = reference("roomId", RoomTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)

    override val primaryKey = PrimaryKey(roomId, itemConfigId)
}
