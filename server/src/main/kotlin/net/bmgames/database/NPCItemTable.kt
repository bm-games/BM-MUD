package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object NPCItemTable : Table("NPCItem") {
    val npcId = reference("NPCId", NPCTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)

    override val primaryKey = PrimaryKey(npcId, itemConfigId)
}
