package net.bmgames.database

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable

/**
 * Length of names
 * */
internal const val NAME_LENGTH = 200

/**
 * Length of long strings (2^16)
 * */
internal const val MAX_VARCHAR = 2 shl 15

/**
 * Length of password hashes
 * */
internal const val PW_LENGTH = 500

/**
 * Length of the registration key. Can be up to 10 chars longer than a username
 * */
internal const val REG_LENGTH = NAME_LENGTH + 10


open class GameReferencingTable(name: String) : IntIdTable(name) {
    val game = reference("gameName", GameTable)
}

open class GameReferencingDAO(id: EntityID<Int>, table: GameReferencingTable) : IntEntity(id) {
    var gameRef by GameDAO referencedOn table.game
}


fun <T> tryOrNull(f: () -> T): T? =
    try {
        f()
    } catch (_: Exception) {
        null
    }

fun <ID : Comparable<ID>, E : Entity<ID>> EntityClass<ID, E>.updateOrCreate(id: ID?, update: E.() -> Unit): E =
    if (id != null) {
        get(id).apply(update)
    } else {
        new(update)
    }


fun <ID : Comparable<ID>, E : Entity<ID>> EntityClass<ID, E>.getOrCreate(id: ID?, update: E.() -> Unit): E =
    if (id != null) {
        get(id)
    } else {
        new(update)
    }

fun <T> Collection<T>.toSized(): SizedIterable<T> = SizedCollection(this)
