package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
/**
 * Represents the Database Table
 * */
object ClassTable : IntIdTable("ClassConfig") {
    val game = reference("gameName", GameTable)
    val name = varchar("className", NAME_LENGTH)
    val description = varchar("description", NAME_LENGTH)
    val healthMultiplier = float("healthMultiplier")
    val damage = integer("damage")
    val attackSpeed = integer("attackSpeed")
}

class ClassDAO (id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<ClassDAO>(ClassTable)

    var game by GameDAO referencedOn ClassTable.game

    val description by ClassTable.description
    val healthMultiplier by ClassTable.healthMultiplier
    val damage by ClassTable.damage
    val attackSpeed by ClassTable.attackSpeed
}
