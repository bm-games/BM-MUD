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
    var name by ItemConfigTable.name
    var type by ItemConfigTable.type

    var effect by ItemConfigTable.effect
    var baseDamage by ItemConfigTable.baseDamage
    var healthModifier by ItemConfigTable.healthModifier
    var damageModifier by ItemConfigTable.damageModifier

    fun toItem(): Item =
        when (type) {
            Type.Weapon -> Weapon(name, baseDamage!!)
            Type.Consumable -> Consumable(name, effect)
            Type.Head -> Equipment(name, healthModifier!!, damageModifier!!, Slot.Head)
            Type.Chest -> Equipment(name, healthModifier!!, damageModifier!!, Slot.Chest)
            Type.Legs -> Equipment(name, healthModifier!!, damageModifier!!, Slot.Legs)
            Type.Boots -> Equipment(name, healthModifier!!, damageModifier!!, Slot.Boots)
        }.setId(id.value)
}

