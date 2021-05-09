package net.bmgames.database

import net.bmgames.state.model.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table


/**
 * Represents the Database Table
 * */
object JoinRequestTable : Table("JoinRequest") {
    val game = reference("gameName", GameTable)
    val user = reference("userName", UserTable)

    override val primaryKey = PrimaryKey(game, user)
}



