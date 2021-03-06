package net.bmgames.state.database


import net.bmgames.state.model.NPC
import net.bmgames.state.model.NPC.Friendly
import net.bmgames.state.model.NPC.Hostile
import net.bmgames.state.model.items
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the Database Table
 * */
object NPCTable : IntIdTable("NPC") {
    val npcConfigId = reference("npcConfigId", NPCConfigTable)
    val roomId = reference("roomId", RoomTable)
    val health = integer("health").nullable()
}

class NPCDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NPCDAO>(NPCTable)

    var health by NPCTable.health
    var npcConfig by NPCConfigDAO referencedOn NPCTable.npcConfigId
    var roomRef by RoomDAO referencedOn NPCTable.roomId
    var items by ItemConfigDAO via NPCItemTable

    fun toNPC(): NPC {
        return when (val npc = npcConfig.toNPC()) {
            is Friendly -> npc.copy(
                items = items.map { it.toItem() },
                id = id.value
            )
            is Hostile -> npc.copy(
                items = items.map { it.toItem() },
                health = health!!,
                id = id.value
            )
        }
    }
}
