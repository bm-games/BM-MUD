package net.bmgames.state.database

import net.bmgames.state.model.Inventory
import net.bmgames.state.model.Item
import net.bmgames.state.model.Item.Weapon
import net.bmgames.state.model.Player
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


/**
 * Represents the Database Table
 * */
object PlayerTable : GameReferencingTable("Player") {
    val avatarId = reference("avatarId", AvatarTable)
    val user = reference("userName", UserTable)
    val room = varchar("room", NAME_LENGTH)
    val health = integer("health")
    val visitedRooms = varchar("visitedRooms", MAX_VARCHAR)
}

class PlayerDAO(id: EntityID<Int>) : GameReferencingDAO(id, PlayerTable) {
    companion object : IntEntityClass<PlayerDAO>(PlayerTable)

    var avatar by AvatarDAO referencedOn PlayerTable.avatarId
    var user by UserDAO referencedOn PlayerTable.user
    var room by PlayerTable.room
    var health by PlayerTable.health
    var visitedRooms: Set<String> by PlayerTable.visitedRooms
        .transform({ it.joinToString("\n") },
            { it.split("\n").filterTo(mutableSetOf(), String::isNotEmpty) })

    var inventory by ItemConfigDAO via PlayerItemTable

    fun toPlayer() = Player.Normal(
        user = user.toUser(),
        avatar = avatar.toAvatar(),
        inventory = inventory.map { it.toItem() }.toInventory(),
        room = room,
        healthPoints = health,
        lastHit = null,
        visitedRooms = visitedRooms,
        id = id.value
    )
}

private fun List<Item>.toInventory(): Inventory =
    Inventory(
        weapon = filterIsInstance<Weapon>().firstOrNull(),
        equipment = filterIsInstance<Item.Equipment>().associateBy { it.slot },
        items = filterIsInstance<Item.Consumable>()
    )

