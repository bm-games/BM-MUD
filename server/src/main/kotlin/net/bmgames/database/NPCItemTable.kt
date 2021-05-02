package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object NPCItemTable : Table("NPCItem") {
    val id = integer("NPCItemId")
    val NPCId = integer("NPCId")
    val itemConfigId = integer("itemConfigId")

    override val primaryKey = PrimaryKey(id, name = "NPCItemId")

}
