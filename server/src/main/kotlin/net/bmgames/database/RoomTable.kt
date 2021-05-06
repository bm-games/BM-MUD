package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RoomTable : IntIdTable("Room") {
    val game = reference("gameName", GameTable)
    val configId = reference("configId", RoomConfigTable)
}


class RoomDAO (id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<NPCDAO>(NPCTable)

    val game by GameDAO referencedOn RoomTable.game
    val config by RoomConfigDAO referencedOn RoomTable.configId
}
