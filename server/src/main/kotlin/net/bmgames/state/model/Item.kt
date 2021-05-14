package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
@optics
sealed class Item {
    abstract val name: String
    abstract val id: Int?

}

@Serializable
@optics
data class Consumable(
    override val name: String,
    val effect: String?,
    override val id: Int? = null,
) : Item()

@Serializable
@optics
data class Equipment(
    override val name: String,
    val healthModifier: Float,
    val damageModifier: Float,
    val slot: Slot,
    override val id: Int? = null,
) : Item() {
    enum class Slot { Head, Chest, Legs, Boots }
}

@Serializable
@optics
data class Weapon(
    override val name: String,
    val damage: Int,
    override val id: Int? = null,
) : Item()

