package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object RoomConfigTable : IntIdTable("RoomConfig") {
    val game = reference("gameName", GameTable)
    val name = varchar("roomName", NAME_LENGTH)
    val message = varchar("message", NAME_LENGTH)

    val northId = reference("northId", RoomConfigTable).nullable()
    val eastId = reference("eastId", RoomConfigTable).nullable()
    val westId = reference("westId", RoomConfigTable).nullable()
    val southId = reference("southId", RoomConfigTable).nullable()
}

class RoomConfigDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomConfigDAO>(RoomConfigTable)

    val game by GameDAO referencedOn RoomTable.game
    val name by RoomConfigTable.name
    val message by RoomConfigTable.message

    val north by RoomConfigDAO optionalReferencedOn RoomConfigTable.northId
    val east by RoomConfigDAO optionalReferencedOn RoomConfigTable.eastId
    val west by RoomConfigDAO optionalReferencedOn RoomConfigTable.westId
    val south by RoomConfigDAO optionalReferencedOn RoomConfigTable.southId
}
