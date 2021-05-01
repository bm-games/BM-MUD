package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object ClassTable : Table("Class") {
    val id = integer("classId")
    val name = varchar("className", NAME_LENGTH)
    val damage = integer("damage")
    val attackSpeed = integer("attackSpeed")
    val description = varchar("description", NAME_LENGTH)

    override val primaryKey = PrimaryKey(id, name = "classId")

}
