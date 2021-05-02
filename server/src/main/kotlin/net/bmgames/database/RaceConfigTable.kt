package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RaceConfigTable : Table("RaceConfig") {
    val id = integer("raceConfigId")
    val game = varchar("gameName", NAME_LENGTH)
    val name = varchar("raceName", NAME_LENGTH)
    val health = integer("health")
    val damageMultiplier = float("damageMultiplier")
    val description = varchar("description", NAME_LENGTH)

    override val primaryKey = PrimaryKey(id, name = "raceConfigId")

}
