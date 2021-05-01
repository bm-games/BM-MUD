package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object PlayerEquipmentTable : Table("PlayerEquipment") {
    val id = integer("playerEquipmentId")
    val playerId = integer("playerId")
    val equipmentConfigId = integer("equipmentConfigId")

    override val primaryKey = PrimaryKey(id, name = "playerEquipmentId")

}
