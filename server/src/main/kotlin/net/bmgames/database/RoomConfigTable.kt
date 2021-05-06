package net.bmgames.database

import net.bmgames.state.model.Room
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object RoomConfigTable : GameReferencingTable("RoomConfig") {
    val name = varchar("roomName", NAME_LENGTH)
    val message = varchar("message", NAME_LENGTH)

    val northId = varchar("northId", NAME_LENGTH).nullable()
    val eastId = varchar("eastId", NAME_LENGTH).nullable()
    val westId = varchar("westId", NAME_LENGTH).nullable()
    val southId = varchar("southId", NAME_LENGTH).nullable()
}

class RoomConfigDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomConfigDAO>(RoomConfigTable)

    var game by GameDAO referencedOn RoomConfigTable.game
    var name by RoomConfigTable.name
    var message by RoomConfigTable.message

    var north by RoomConfigTable.northId
    var east by RoomConfigTable.eastId
    var west by RoomConfigTable.westId
    var south by RoomConfigTable.southId

}
