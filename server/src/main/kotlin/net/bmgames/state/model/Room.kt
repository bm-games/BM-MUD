package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

/**
 *
 * */
@Serializable
@optics
data class Room(
    val name: String,
    val message: String,
    val north: String? = null,
    val east: String? = null,
    val south: String? = null,
    val west: String? = null,
    val items: List<Item> = emptyList(),
    val npcs: Map<String, NPC> = emptyMap()
) : IdEntity()
