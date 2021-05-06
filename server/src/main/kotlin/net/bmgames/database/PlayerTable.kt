package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object PlayerTable : IntIdTable("Player") {
    val avatarId = reference("avatarId", AvatarTable)
    val game = reference("gameName", GameTable)
    val user = reference("userName", UserTable)
    val roomId = reference("roomId", RoomTable)
    val health = integer("health")
}

class PlayerDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlayerDAO>(PlayerTable)

    var avatar by AvatarDAO referencedOn PlayerTable.avatarId
    var game by GameDAO referencedOn PlayerTable.game
    var user by UserDAO referencedOn PlayerTable.user
    var room by RoomDAO referencedOn PlayerTable.roomId

    val health by PlayerTable.health
}
