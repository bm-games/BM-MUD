package net.bmgames.state.database

import net.bmgames.state.model.Room
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Represents the Database Table
 * */
object RoomTable : GameReferencingTable("Room") {
    val name = varchar("roomName", NAME_LENGTH)
    val message = varchar("message", NAME_LENGTH)
    val northId = varchar("northId", NAME_LENGTH).nullable()
    val eastId = varchar("eastId", NAME_LENGTH).nullable()
    val westId = varchar("westId", NAME_LENGTH).nullable()
    val southId = varchar("southId", NAME_LENGTH).nullable()
}


class RoomDAO(id: EntityID<Int>) : GameReferencingDAO(id, RoomTable) {
    companion object : IntEntityClass<RoomDAO>(RoomTable)

    var name by RoomTable.name
    var message by RoomTable.message
    var north by RoomTable.northId
    var east by RoomTable.eastId
    var west by RoomTable.westId
    var south by RoomTable.southId

    // Many to many
    var items by ItemConfigDAO via RoomItemTable

    // One to many
    private val npcs by NPCDAO referrersOn NPCTable.roomId

    fun toRoom(): Room =
        Room(
            name,
            message,
            north,
            east,
            west,
            south,
            items.map { it.toItem() },
            npcs.map { it.toNPC() }.associateBy { it.name }
        )

}
