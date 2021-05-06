package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object NPCConfigTable : IntIdTable("NPCConfig") {
    val game = reference("gameName", GameTable.id)

    val name = varchar("NPCName", NAME_LENGTH)
    val friendly = bool("friendly")
    val health = integer("health").nullable()
    val damage = integer("damage").nullable()
    val message = varchar("message", NAME_LENGTH).nullable()
    val command = varchar("command", NAME_LENGTH).nullable()
}

class NPCConfigDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NPCConfigDAO>(NPCConfigTable)

    var game by GameDAO referencedOn NPCConfigTable.game

    val name by NPCConfigTable.name
    val friendly by NPCConfigTable.friendly
    val health by NPCConfigTable.health
    val damage by NPCConfigTable.damage
    val message by NPCConfigTable.message
    val command by NPCConfigTable.command
}
