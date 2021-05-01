package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object ItemConfigTable : Table("ItemConfig") {
    val id = integer("itemConfigId")
    val isConsumable = bool("isConsumable")
    val effect = varchar("effect", NAME_LENGTH)

    override val primaryKey = PrimaryKey(id, name = "itemConfigId")

}
