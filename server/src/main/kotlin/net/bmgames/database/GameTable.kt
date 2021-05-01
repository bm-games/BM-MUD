package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object GameTable : Table("Game") {
    val name = varchar("gameName", NAME_LENGTH)
    val config = varchar("gameConfig", NAME_LENGTH)
    val startRoom = varchar("startRoom", NAME_LENGTH)
    val dungeonMaster = varchar("dungeonMaster", NAME_LENGTH)

    override val primaryKey = PrimaryKey(name, name = "gameName")

}
