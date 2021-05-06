package net.bmgames.state.model

import kotlinx.serialization.Serializable

@Serializable
data class Avatar(
    val name: String,
    val race: Race,
    val clazz: Clazz
) : IdEntity()

@Serializable
data class Race(
    val name: String,
    val description: String,
    val health: Int,
    val damageModifier: Float,
): IdEntity()

@Serializable
data class Clazz(
    val name: String,
    val description: String,
    val healthMultiplier: Float,
    val damage: Int,
    val attackSpeed: Int
): IdEntity()
