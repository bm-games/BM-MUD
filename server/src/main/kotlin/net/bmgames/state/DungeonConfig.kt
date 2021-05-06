package net.bmgames.state

import kotlinx.serialization.Serializable
import net.bmgames.state.model.*

@Serializable
data class DungeonConfig(
    val name: String,

    val races: List<Race>,
    val classes: List<Clazz>,
    val commandConfig: CommandConfig,
    val npcConfigs: Map<String, NPC>,
    val itemConfigs: Map<String, Item>,

    val startRoom: String,
    val rooms: Map<String, Room>
)
