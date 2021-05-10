package net.bmgames.state.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object NPCItemTable : Table("NPCItem") {
    val npcId = reference("NPCId", NPCTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)

    override val primaryKey = PrimaryKey(npcId, itemConfigId)
}
