package net.bmgames.state.database

import org.jetbrains.exposed.sql.Table


/**
 * Represents the Database Table
 * */
object PlayerItemTable : Table("PlayerItem") {
    val playerId = reference("playerId", PlayerTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)

    override val primaryKey = PrimaryKey(playerId, itemConfigId)
}
