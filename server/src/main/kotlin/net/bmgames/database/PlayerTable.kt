package net.bmgames.database

import net.bmgames.state.model.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


/**
 * Represents the Database Table
 * */
object PlayerTable : GameReferencingTable("Player") {
    val avatarId = reference("avatarId", AvatarTable)
    val user = reference("userName", UserTable)
    val room = varchar("room", NAME_LENGTH)
    val health = integer("health")
}

class PlayerDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlayerDAO>(PlayerTable)

    var game by GameDAO referencedOn PlayerTable.game
    var avatar by AvatarDAO referencedOn PlayerTable.avatarId
    var room by PlayerTable.room
    var health by PlayerTable.health

    var user by UserDAO referencedOn PlayerTable.user

    var inventory by ItemConfigDAO via PlayerItemTable
    var visitedRooms by VisitedRoomDAO via VisitedRoomsTable

    val health by PlayerTable.health
}
