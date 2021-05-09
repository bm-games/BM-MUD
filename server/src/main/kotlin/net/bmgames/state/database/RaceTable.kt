package net.bmgames.state.database

import net.bmgames.state.model.Race
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object RaceTable : IntIdTable("Race") {
    val game = reference("gameName", GameTable)
    val name = varchar("raceName", NAME_LENGTH)
    val description = varchar("description", NAME_LENGTH)
    val health = integer("health")
    val damageMultiplier = float("damageMultiplier")
}

class RaceDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RaceDAO>(RaceTable)

    var gameRef by GameDAO referencedOn RaceTable.game

    var name by RaceTable.name
    var description by RaceTable.description
    var health by RaceTable.health
    var damageMultiplier by RaceTable.damageMultiplier

    fun toRace(): Race = Race(name, description, health, damageMultiplier, id.value)
}
