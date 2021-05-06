package net.bmgames.database


import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable


/**
 * Represents the Database Table
 * */
object GameTable : IdTable<String>("Game") {
    override val id = varchar("gameName", NAME_LENGTH).entityId()
    val startRoom = varchar("startRoom", NAME_LENGTH)
    val master = reference("dungeonMaster", GameTable)
}

class GameDAO(name: EntityID<String>) : Entity<String>(name) {
    companion object : EntityClass<String, GameDAO>(GameTable)

    val name by GameTable.id
    val startRoom by GameTable.startRoom
    val master by UserDAO referrersOn GameTable.master
}
