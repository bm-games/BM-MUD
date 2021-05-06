package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object NPCItemTable : IntIdTable("NPCItem") {
    val npcId = reference("NPCId", NPCTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)
}


class NPCItemDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NPCItemDAO>(NPCItemTable)

    val npc by NPCDAO referencedOn NPCItemTable.npcId
    val item by ItemConfigDAO referencedOn NPCItemTable.itemConfigId
}
