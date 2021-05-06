package net.bmgames.database

import org.jetbrains.exposed.sql.Table

/**
 * Represents the Database Table
 * */
object StartItemTable : Table("StartItem") {
    val game = reference("gameName", GameTable)
    val itemConfigId = reference("itemConfigId", ItemConfigTable)

    override val primaryKey = PrimaryKey(game, itemConfigId)
}
/*
class StartItemDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StartItemDAO>(StartItemTable)

    var gameName by StartItemTable.game
    var itemConfig by ItemConfigDAO referencedOn StartItemTable.itemConfigId
}*/
