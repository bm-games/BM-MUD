package net.bmgames.state.model

import kotlinx.serialization.Serializable

@Serializable
data class CommandConfig(
    val aliases: Map<String, String> = emptyMap(),
    val customCommands: Map<String, String> = emptyMap()
)
