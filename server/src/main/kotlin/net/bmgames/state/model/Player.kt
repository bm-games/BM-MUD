package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable
import net.bmgames.authentication.User

@Serializable
@optics
sealed class Player {
    abstract val user: User
    abstract val ingameName: String

    @Serializable
    @optics
    data class Master(override val user: User) : Player() {
        override val ingameName = user.username
    }

    @Serializable
    @optics
    data class Normal(
        val id: Int? = null,
        override val user: User,
        val avatar: Avatar,

        val inventory: Inventory,
        val room: String,

        val healthPoints: Int,
        val lastHit: Long?,
        val visitedRooms: Set<String>
    ) : Player() {
        override val ingameName = avatar.name
        val damage: Float
            get() = (avatar.clazz.damage + (inventory.weapon?.damage ?: 0)) * avatar.race.damageModifier

        fun canHit(): Boolean =
            lastHit == null ||
                    lastHit + HIT_TIMEFRAME / avatar.clazz.attackSpeed <= System.currentTimeMillis()

    }
}

/**
 * In this time range the player can hit [Clazz.attackSpeed] times
 * */
const val HIT_TIMEFRAME = 20L * 1000L
