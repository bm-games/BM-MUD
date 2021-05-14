package net.bmgames

import org.jetbrains.annotations.PropertyKey
import java.util.*

fun message(@PropertyKey(resourceBundle = "Language") id: String) =
    ResourceBundle.getBundle("Language").getString(id) ?: "###$id###"

fun message(@PropertyKey(resourceBundle = "Language") id: String, vararg args: Any?) =
    message(id).format(args)
