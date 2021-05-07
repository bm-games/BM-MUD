package net.bmgames.state.model

import kotlinx.serialization.Serializable

@Serializable
data class Avatar(
    val name: String,
    val race: Race,
    val clazz: Clazz,
    val id: Int? = null,
)

@Serializable
data class Race(
    val name: String,
    val description: String,
    val health: Int,
    val damageModifier: Float,
    val id: Int? = null,
)

@Serializable
data class Clazz(
    val name: String,
    val description: String,
    val healthMultiplier: Float,
    val damage: Int,
    val attackSpeed: Int,
    val id: Int? = null,
)
