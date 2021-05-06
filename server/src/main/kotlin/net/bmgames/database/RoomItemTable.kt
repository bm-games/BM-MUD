package net.bmgames.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RoomItemTable : IntIdTable("RoomItem") {
    val roomId = reference("roomId", RoomTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)
//    override val primaryKey = PrimaryKey(roomId, itemConfigId)
}

/*
class RoomItemDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomItemDAO>(RoomItemTable)

    val room by RoomDAO referencedOn RoomItemTable.roomId
    val itemConfig by ItemConfigDAO referencedOn RoomItemTable.itemConfigId
}
*/
