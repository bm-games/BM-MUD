package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable
import net.bmgames.state.model.Item.Equipment

@optics
@Serializable
data class Inventory(
    val weapon: Item.Weapon? = null,
    val equipment: Map<Equipment.Slot, Equipment> = emptyMap(),
    val items: List<Item> = emptyList()
){
    val healthModifier: Float
      get() = equipment.values.fold(1f) { all, it -> all * it.healthModifier }

    fun allItems(): List<Item> = listOfNotNull(weapon) + equipment.values + items
}
