package net.bmgames

import org.jetbrains.annotations.PropertyKey
import java.util.*

fun message(@PropertyKey(resourceBundle = "Language") id: String) =
    ResourceBundle.getBundle("Messages").getString(id) ?: "###$id###"
