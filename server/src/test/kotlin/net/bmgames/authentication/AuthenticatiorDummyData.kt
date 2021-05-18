package net.bmgames.authentication

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmgames.authentication.User
import net.bmgames.communication.Notifier
import net.bmgames.state.DungeonConfig
import net.bmgames.state.model.*
import net.bmgames.state.model.Direction.NORTH
import net.bmgames.state.model.Direction.SOUTH


val mail = "bingo@bongo.de"
val name = "bingo"
val pw = "1234"


val testuser = User(
    username = "bingo",
    email = "bingo@bongo.de",
    passwordHash = "1234",
    registrationKey = null)

