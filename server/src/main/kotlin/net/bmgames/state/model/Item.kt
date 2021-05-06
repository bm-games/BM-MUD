package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
@optics
sealed class Item {
    abstract val name: String
}

@Serializable
@optics
data class Consumable(
    override val name: String,
    val effect: String
) : Item()

@Serializable
@optics
data class Equipment(
    override val name: String,
    val healthModifier: Float,
    val damageModifier: Float,
    val slot: Slot,
) : Item() {
    enum class Slot { Head, Chest, Legs, Boots }
}

@Serializable
@optics
data class Weapon(
    override val name: String,
    val damage: Int
) : Item()
