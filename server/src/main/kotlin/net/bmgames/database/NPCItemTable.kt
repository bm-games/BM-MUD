package net.bmgames.database

import org.jetbrains.exposed.sql.Table

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
