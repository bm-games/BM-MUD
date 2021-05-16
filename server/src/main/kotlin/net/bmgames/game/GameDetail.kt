package net.bmgames.game

import kotlinx.serialization.Serializable
import net.bmgames.state.model.Clazz
import net.bmgames.state.model.Race

@Serializable
data class GameDetail(
    val players: List<PlayerOverview>,
    val races: List<Race>,
    val classes: List<Clazz>,
    val isMaster: Boolean
)

