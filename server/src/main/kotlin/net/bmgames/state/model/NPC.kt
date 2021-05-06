package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
@optics
sealed class NPC {
    abstract val name: String
    abstract val items: List<Item>

    @Serializable
    @optics
    data class Hostile(
        override val name: String,
        override val items: List<Item>,
        val health: Int,
        val damage: Int
    ) : NPC()

    @Serializable
    @optics
    data class Friendly(
        override val name: String,
        override val items: List<Item>,
        val commandOnInteraction: String,
        val messageOnTalk: String
    ) : NPC()

}
