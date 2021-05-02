package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */

enum class Type
{
    Alias,
    Custom
}

object CommandConfigTable : Table("CommandConfig") {
    val id = integer("roomConfigId")
    val game = varchar("gameName", NAME_LENGTH)
    val new = varchar("newCommand", NAME_LENGTH)
    val original = varchar("originalCommand", NAME_LENGTH)
    val type = enumerationByName("type", NAME_LENGTH, Type::class)

    override val primaryKey = PrimaryKey(id, name = "commandConfigId")

}
