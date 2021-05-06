package net.bmgames.database

import net.bmgames.database.ItemConfigTable.Type
import net.bmgames.state.model.*
import net.bmgames.state.model.Equipment.Slot
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object ItemConfigTable : GameReferencingTable("ItemConfig") {
    val name = varchar("itemName", NAME_LENGTH)
    val type = enumerationByName("type", NAME_LENGTH, Type::class)

    val effect = varchar("effect", NAME_LENGTH).nullable()
    val baseDamage = integer("baseDamage").nullable()
    val healthModifier = float("healthModifier").nullable()
    val damageModifier = float("damageModifier").nullable()

    enum class Type { Head, Chest, Legs, Boots, Weapon, Consumable }
}


class ItemConfigDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemConfigDAO>(ItemConfigTable)

    var game by GameDAO referencedOn ItemConfigTable.game
    val name by ItemConfigTable.name
    val type by ItemConfigTable.type

    val isConsumable by ItemConfigTable.isConsumable
    val effect by ItemConfigTable.effect
    val baseDamage by ItemConfigTable.baseDamage
    val healthModifier by ItemConfigTable.healthModifier
    val damageModifier by ItemConfigTable.damageModifier
}
