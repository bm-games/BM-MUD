package net.bmgames.database


import net.bmgames.state.model.NPC
import net.bmgames.state.model.NPC.Friendly
import net.bmgames.state.model.NPC.Hostile
import net.bmgames.state.model.health
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

    var npcConfig by NPCConfigDAO referencedOn NPCTable.npcConfigId

    var room by RoomDAO referencedOn NPCTable.roomId
    var health by NPCTable.health

    var items by ItemConfigDAO via NPCItemTable

    fun toNPC(): NPC {
        return when (val npc = npcConfig.toNPC()) {
            is Friendly -> Friendly.items.set(npc, items.map { it.toItem() })
            is Hostile -> npc.copy(
                items = items.map { it.toItem() },
                health = health!!
            )
        }
    }
}
