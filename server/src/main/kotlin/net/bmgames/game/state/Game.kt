package net.bmgames.game.state

import kotlinx.serialization.Serializable
import net.bmgames.configurator.model.DungeonConfig

/**
 * @property users Maps every allowed user to his avatars
 * */
@Serializable
data class Game(
    val config: DungeonConfig,
    val master: Player.Master,
    val users: Map<String, List<String>>,
    val onlinePlayers: Map<String, Player>,
) {
    val name = config.name
}

fun Game.isMasterOnline() = onlinePlayers.containsKey(master.ingameName)
