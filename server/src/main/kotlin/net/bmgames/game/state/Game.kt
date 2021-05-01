package net.bmgames.game.state

import kotlinx.serialization.Serializable
import net.bmgames.configurator.model.DungeonConfig

/**
 * @property allowedPlayers Maps every
 * */
@Serializable
data class Game(
    val config: DungeonConfig,
    val master: Player.Master,
    val players: Map<String, Avatar>,
    val onlinePlayers: Map<String, Player.Normal>,
) {
    val name = config.name
}
