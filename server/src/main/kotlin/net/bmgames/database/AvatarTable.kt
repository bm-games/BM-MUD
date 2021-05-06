package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object AvatarTable : Table("Avatar") {
    val id = integer("avatarId")
    val name = varchar("avatarName", NAME_LENGTH)
    val raceId = reference("raceId", RaceTable)
    val classId = reference("classId", ClassTable)
}

class AvatarDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AvatarDAO>(AvatarTable)

    val name by AvatarTable.name
    val race by RaceDAO referencedOn AvatarTable.raceId
    val clazz by ClassDAO referencedOn AvatarTable.classId
}
