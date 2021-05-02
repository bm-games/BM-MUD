package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object NPCConfigtable : Table("NPCConfig") {
    val id = integer("NPCConfigId")
    val game = varchar("gameName", NAME_LENGTH)
    val name = varchar("NPCName", NAME_LENGTH)
    val type = varchar("type", NAME_LENGTH)
    val damageMultiplier = float("damageMultiplier")
    val friendly = bool("friendly")
    val health = integer("health").nullable()
    val damage = integer("damage").nullable()
    val message = varchar("message", NAME_LENGTH).nullable()
    val command = varchar("command", NAME_LENGTH).nullable()

    override val primaryKey = PrimaryKey(id, name = "NPCConfigId")

}
