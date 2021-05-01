package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object NPCConfig : Table("NPCConfig") {
    val id = integer("NPCConfigId")
    val name = varchar("NPCName", NAME_LENGTH)
    val type = varchar("type", NAME_LENGTH)
    val damageMultiplier = float("damageMultiplier")
    val description = varchar("description", NAME_LENGTH)

    override val primaryKey = PrimaryKey(id, name = "NPCConfigId")

}
