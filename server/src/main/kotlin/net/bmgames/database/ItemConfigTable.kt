package net.bmgames.database

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

/**
 * Represents the Database Table
 * */

enum class Slot
{
    Head,
    Chest,
    Legs,
    Boots,
    Weapon
}

object ItemConfigTable : Table("ItemConfig") {
    val id = integer("itemConfigId")
    val game = varchar("gameName", NAME_LENGTH)
    val name = varchar("itemName", NAME_LENGTH)
    val isConsumable = bool("isConsumable").nullable()
    val effect = varchar("effect", NAME_LENGTH).nullable()
    val baseDamage = integer("baseDamage").nullable()
    val healthModifier = float("healthModifier").nullable()
    val damageModifier = float("damageModifier").nullable()
    val slot = enumerationByName("slot", NAME_LENGTH, Slot::class).nullable()

    override val primaryKey = PrimaryKey(id, name = "itemConfigId")

}
