package net.bmgames.game

import kotlinx.serialization.Serializable
import net.bmgames.state.model.Clazz
import net.bmgames.state.model.Race

/**
 * DTO to give some details for a concrete game for a concrete user.
 *
 * @property players All Players of the user
 * @property races All races in the game. Used for the avatar configurator
 * @property classes All classes in the game. Used for the avatar configurator
 * @property isMaster If the user is also the master
 * */
@Serializable
data class GameDetail(
    val players: List<PlayerOverview>,
    val races: List<Race>,
    val classes: List<Clazz>,
    val isMaster: Boolean
)

