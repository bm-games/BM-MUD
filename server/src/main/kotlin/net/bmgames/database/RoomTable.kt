package net.bmgames.database

import net.bmgames.state.model.Room
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object RoomTable :  IntIdTable("Room") {
    val game = reference("gameName", GameTable)
    val configId = reference("configId", RoomConfigTable)
}


class RoomDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomDAO>(RoomTable)

    var game by GameDAO referencedOn RoomTable.game
    var config by RoomConfigDAO referencedOn RoomTable.configId

    var items by ItemConfigDAO via RoomItemTable
    val npcs by NPCDAO referrersOn NPCTable.roomId

    fun toRoom(): Room = with(config) {
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
}
