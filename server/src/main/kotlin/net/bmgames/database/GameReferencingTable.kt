package net.bmgames.database

import org.jetbrains.exposed.dao.id.IntIdTable

open class GameReferencingTable(name: String): IntIdTable(name) {
    val game = reference("gameName", GameTable)
}
