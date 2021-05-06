package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object CommandConfigTable : IntIdTable("CommandConfig") {
    val game = reference("gameName", GameTable)
    val type = enumerationByName("type", NAME_LENGTH, Type::class)

    val new = varchar("newCommand", NAME_LENGTH)
    val original = varchar("originalCommand", NAME_LENGTH)

    enum class Type { Alias, Custom }
}

class CommandDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CommandDAO>(CommandConfigTable)

    var game by CommandDAO referencedOn CommandConfigTable.game

    val type by CommandConfigTable.type
    val new by CommandConfigTable.new
    val original by CommandConfigTable.original
}

