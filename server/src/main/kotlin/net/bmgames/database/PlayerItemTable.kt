package net.bmgames.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


/**
 * Represents the Database Table
 * */
object PlayerItemTable : IntIdTable("PlayerItem") {
    val playerId = reference("playerId", PlayerTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)
}

class PlayerItemDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlayerItemDAO>(PlayerItemTable)

    var playerId by PlayerDAO referencedOn PlayerItemTable.playerId
    var itemConfigId by ItemConfigDAO referencedOn PlayerItemTable.itemConfigId
}
