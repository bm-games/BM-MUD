package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object CommandTable : GameReferencingTable("Command") {
    val type = enumerationByName("type", NAME_LENGTH, Type::class)

    val new = varchar("newCommand", NAME_LENGTH)
    val original = varchar("originalCommand", NAME_LENGTH)

    enum class Type { Alias, Custom }
}

class CommandDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CommandDAO>(CommandTable)

    var game by GameDAO referencedOn CommandTable.game

    var type by CommandTable.type
    var new by CommandTable.new
    var original by CommandTable.original
}

