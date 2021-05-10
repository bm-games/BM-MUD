package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@optics
@Serializable
data class Inventory(
    val weapon: Weapon?,
    val equipment: Map<Equipment.Slot, Equipment>,
    val items: List<Item>
)
