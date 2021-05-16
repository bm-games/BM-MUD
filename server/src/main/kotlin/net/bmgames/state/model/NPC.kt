package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
@optics
sealed class NPC {
    abstract val name: String
    abstract val items: List<Item>
    abstract val id: Int?

    @Serializable
    @optics
    data class Hostile(
        override val name: String,
        override val items: List<Item>,
        val health: Int,
        val damage: Int,
        val lastDamageDealt: Long? = null,
        override val id: Int? = null,
    ) : NPC() {

        fun nextAttackTimePoint(): Long =
            if (lastDamageDealt == null) {
                System.currentTimeMillis()
            } else {
                lastDamageDealt + ATTACK_COOLDOWN
            }

    }

    @Serializable
    @optics
    data class Friendly(
        override val name: String,
        override val items: List<Item>,
        val commandOnInteraction: String,
        val messageOnTalk: String,
        override val id: Int? = null,
    ) : NPC()

}


