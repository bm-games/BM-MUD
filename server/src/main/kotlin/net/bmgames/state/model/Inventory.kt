package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable
import net.bmgames.state.model.Item.Equipment

@optics
@Serializable
data class Inventory(
    val weapon: Item.Weapon?,
    val equipment: Map<Equipment.Slot, Equipment>,
    val items: List<Item>
){
    fun allItems(): List<Item> = listOfNotNull(weapon) + equipment.values + items
}
