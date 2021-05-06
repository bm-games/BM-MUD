package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object VisitedRoomsTable : IntIdTable("VisitedRooms") {
    val game = reference("gameName", GameTable)
    val playerId = reference("playerId", PlayerTable)
    val roomId = reference("roomId", RoomTable)
}

class VisitedRoomDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VisitedRoomDAO>(VisitedRoomsTable)

    val game by GameDAO referencedOn VisitedRoomsTable.game
    val player by PlayerDAO referencedOn VisitedRoomsTable.playerId
    val room by RoomDAO referencedOn VisitedRoomsTable.roomId
}
