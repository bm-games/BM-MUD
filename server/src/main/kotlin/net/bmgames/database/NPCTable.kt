package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object NPCTable : Table("NPC") {
    val id = integer("NPCId")
    val NPCConfigId = integer("NPCConfigId")
    val roomId = integer("roomId")
    val health = integer("health").nullable()

    override val primaryKey = PrimaryKey(id, name = "NPCId")

}
