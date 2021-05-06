package net.bmgames.database


import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object NPCTable : Table("NPC") {
    val id = integer("NPCId")
    val NPCConfigId = integer("NPCConfigId")
    val roomId = integer("roomId")
    val health = integer("health").nullable()
}

class NPCDAO (id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<NPCDAO>(NPCTable)

    val npcConfig by NPCConfigDAO referencedOn NPCTable.npcConfigId
    val room  by RoomDAO referencedOn NPCTable.roomId
    val health by NPCTable.health
}
