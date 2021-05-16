package net.bmgames.state.database

import net.bmgames.state.model.NPC
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Represents the Database Table
 * */
object NPCConfigTable : GameReferencingTable("NPCConfig") {
    val name = varchar("NPCName", NAME_LENGTH)
    val friendly = bool("friendly")
    val health = integer("health").nullable()
    val damage = integer("damage").nullable()
    val message = varchar("message", NAME_LENGTH).nullable()
    val command = varchar("command", NAME_LENGTH).nullable()
}

class NPCConfigDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NPCConfigDAO>(NPCConfigTable)

    var gameRef by GameDAO referencedOn NPCConfigTable.game

    var name by NPCConfigTable.name

    var friendly by NPCConfigTable.friendly
    var maxHealth by NPCConfigTable.health
    var damage by NPCConfigTable.damage
    var message by NPCConfigTable.message
    var command by NPCConfigTable.command

    fun toNPC(): NPC =
        if (friendly) {
            NPC.Friendly(name, emptyList(), command!!, message!!)
        } else {
            NPC.Hostile(name, emptyList(), maxHealth!!, damage!!, null)
        }
}
