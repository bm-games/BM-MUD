package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@optics
@Serializable
data class Inventory(
    val weapon: Weapon? = null,
    val equipment: Map<Equipment.Slot, Equipment> = emptyMap(),
    val items: List<Item> = emptyList()
) {
    val healthModifier: Float
        get() = equipment.values.fold(1f) { all, it -> all * it.healthModifier }

    fun allItems(): List<Item> = listOfNotNull(weapon) + equipment.values + items

    fun isFull(): Boolean = items.size >= INVENTORY_SIZE
}
