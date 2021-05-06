package net.bmgames.database

import net.bmgames.state.model.Avatar
import net.bmgames.state.model.setId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object AvatarTable : IntIdTable("Avatar") {
    val name = varchar("avatarName", NAME_LENGTH)
    val raceId = reference("raceId", RaceTable)
    val classId = reference("classId", ClassTable)
}

class AvatarDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AvatarDAO>(AvatarTable)

    var name by AvatarTable.name
    var race by RaceDAO referencedOn AvatarTable.raceId
    var clazz by ClassDAO referencedOn AvatarTable.classId

    fun toAvatar(): Avatar = Avatar(name, race.toRace(), clazz.toClass()).setId(id.value)
}
