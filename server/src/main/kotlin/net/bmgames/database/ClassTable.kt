package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object ClassConfigTable : Table("ClassConfig") {
    val id = integer("classConfigId")
    val game = varchar("gameName", NAME_LENGTH)
    val name = varchar("className", NAME_LENGTH)
    val description = varchar("description", NAME_LENGTH)
    val healthMultiplier = float("healthMultiplier")
    val damage = integer("damage")
    val attackSpeed = integer("attackSpeed")
}
