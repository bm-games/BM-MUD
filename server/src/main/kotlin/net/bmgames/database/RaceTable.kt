package net.bmgames.database

import net.bmgames.state.model.Race
import net.bmgames.state.model.setId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object RaceTable : IntIdTable("RaceConfig") {
    val game = reference("gameName", GameTable)
    val name = varchar("raceName", NAME_LENGTH)
    val description = varchar("description", NAME_LENGTH)
    val health = integer("health")
    val damageMultiplier = float("damageMultiplier")
}

class RaceDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RaceDAO>(RaceTable)

    var game by GameDAO referencedOn RaceTable.game

    val description by RaceTable.description
    val health by RaceTable.health
    val damageMultiplier by RaceTable.damageMultiplier
}
