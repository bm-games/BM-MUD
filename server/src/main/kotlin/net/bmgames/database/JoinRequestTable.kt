package net.bmgames.database

import net.bmgames.state.model.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object JoinRequestTable : GameReferencingTable("JoinRequest") {
    val user = reference("userName", UserTable)
}



