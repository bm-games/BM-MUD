package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object EquipmentConfigTable : Table("EquipmentConfig") {
    val id = integer("equipmentConfigId")
    val baseDamage = integer("baseDamage")

    override val primaryKey = PrimaryKey(id, name = "equipmentConfigId")

}
