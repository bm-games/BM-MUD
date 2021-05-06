package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object VisitedRoomsTable : GameReferencingTable("VisitedRooms") {
    val playerId = reference("playerId", PlayerTable)
    val room = varchar("room", NAME_LENGTH)
}

class VisitedRoomDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VisitedRoomDAO>(VisitedRoomsTable)

    var game by GameDAO referencedOn VisitedRoomsTable.game
    var player by PlayerDAO referencedOn VisitedRoomsTable.playerId
    var room by VisitedRoomsTable.room
}
