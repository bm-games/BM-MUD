package net.bmgames.database

import net.bmgames.state.model.Clazz
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Represents the Database Table
 * */
object ClassTable : GameReferencingTable("Class") {
    val name = varchar("className", NAME_LENGTH)
    val description = varchar("description", NAME_LENGTH)
    val healthMultiplier = float("healthMultiplier")
    val damage = integer("damage")
    val attackSpeed = integer("attackSpeed")
}

class ClassDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClassDAO>(ClassTable)

    var gameRef by GameDAO referencedOn ClassTable.game

    var name by ClassTable.name
    var description by ClassTable.description
    var healthMultiplier by ClassTable.healthMultiplier
    var damage by ClassTable.damage
    var attackSpeed by ClassTable.attackSpeed

    fun toClass(): Clazz = Clazz(name, description, healthMultiplier, damage, attackSpeed, id.value)
}
