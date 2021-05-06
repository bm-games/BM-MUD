package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object RoomItemTable : IntIdTable("RoomItem") {
    val roomId = reference("roomId", RoomTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)
}

class RoomItemDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomItemDAO>(RoomItemTable)

    val room by RoomDAO referencedOn RoomItemTable.roomId
    val itemConfig by ItemConfigDAO referencedOn RoomItemTable.itemConfigId
}
