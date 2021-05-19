package net.bmgames

import net.bmgames.state.model.Consumable
import net.bmgames.state.model.Equipment
import net.bmgames.state.model.Item
import net.bmgames.state.model.Weapon
import org.jetbrains.annotations.PropertyKey
import java.util.*

fun message(@PropertyKey(resourceBundle = "Language") id: String) =
    ResourceBundle.getBundle("Language").getString(id) ?: "###$id###"

fun message(@PropertyKey(resourceBundle = "Language") id: String, vararg args: Any?): String =
    try {
        message(id).format(*args)
    } catch (e: IllegalFormatException) {
        e.printStackTrace()
        e.localizedMessage
    }

fun Item.translate() = when (this) {
    is Consumable -> name
    is Weapon -> "Waffe"
    is Equipment -> when (slot) {
        Equipment.Slot.Head -> "Helm"
        Equipment.Slot.Chest -> "Brustplatte"
        Equipment.Slot.Legs -> "Hose"
        Equipment.Slot.Boots -> "Schuhe"
    }
}
