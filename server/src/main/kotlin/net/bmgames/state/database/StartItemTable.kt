package net.bmgames.state.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object StartItemTable : Table("StartItem") {
    val game = reference("gameName", GameTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)

    override val primaryKey = PrimaryKey(game, itemConfigId)
}
