package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object RaceTable : Table("Race") {
    val id = integer("raceId")
    val name = varchar("raceName", NAME_LENGTH)
    val health = integer("health")
    val damageMultiplier = float("damageMultiplier")
    val description = varchar("description", NAME_LENGTH)

    override val primaryKey = PrimaryKey(id, name = "raceId")

}
