package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object PlayerItemTable : Table("PlayerItem") {
    val id = integer("playerItemId")
    val playerId = integer("playerId")
    val itemConfigId = integer("itemConfigId")

    override val primaryKey = PrimaryKey(id, name = "playerItemId")

}
