package net.bmgames.state.database

import org.jetbrains.exposed.sql.Table


/**
 * Represents the Database Table
 * */
object JoinRequestTable : Table("JoinRequest") {
    val game = reference("gameName", GameTable)
    val user = reference("userName", UserTable)

    override val primaryKey = PrimaryKey(game, user)
}



