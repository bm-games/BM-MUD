package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object NPCConfigTable : IntIdTable("NPCConfig") {
    val game = reference("gameName", GameTable.id)

    val name = varchar("NPCName", NAME_LENGTH)
    val friendly = bool("friendly")
    val health = integer("health").nullable()
    val damage = integer("damage").nullable()
    val message = varchar("message", NAME_LENGTH).nullable()
    val command = varchar("command", NAME_LENGTH).nullable()

    override val primaryKey = PrimaryKey(id, name = "NPCConfigId")

}
