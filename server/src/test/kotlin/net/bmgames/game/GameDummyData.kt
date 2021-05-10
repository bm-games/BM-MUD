package net.bmgames.game

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmgames.authentication.User
import net.bmgames.state.DungeonConfig
import net.bmgames.state.model.*

val MASTER = Player.Master(
    User(username = "master", email = "master@master.de", passwordHash = "1234", registrationKey = null)
)
val PLAYER by lazy {
    Player.Normal(
        user = User(username = "player1", email = "email@email.de", passwordHash = "asd", registrationKey = null),
        avatar = Avatar(
            name = "georkina",
            race = GAME_WITHOUT_PLAYER.races[0],
            clazz = GAME_WITHOUT_PLAYER.classes[0]
        ),
        inventory = Inventory(null, emptyMap(), emptyList()),
        room = GAME_WITHOUT_PLAYER.startRoom,
        healthPoints = 10,
        lastHit = null,
        visitedRooms = emptySet()
    )
}

val items = mapOf(
    "Apfel" to Consumable("Apfel", "heal \$player 10"),
    "Diamond Helmet" to Equipment("Diamond Helmet", 10f, 1f, Equipment.Slot.Head),
    "Wooden Sword" to Weapon("Wooden Sword", 1),
)

val npcs = mapOf(
    "geork" to NPC.Friendly("geork", listOf(items["Apfel"]!!), "heal \$player 1", "Halloooooooooo"),
    "georkina" to NPC.Hostile("georkina", listOf(items["Diamond Helmet"]!!), 100, 100000),
)

val GAME_WITHOUT_PLAYER = Game(
    name = "Test game",
    races = listOf(
        Race(
            name = "race",
            description = "Its a race",
            health = 10,
            damageModifier = 1.5f
        )
    ),
    classes = listOf(
        Clazz(
            name = "class",
            description = "Its a class",
            healthMultiplier = 1.5f,
            damage = 10,
            attackSpeed = 1
        )
    ),
    commandConfig = CommandConfig(),
    itemConfigs = items,
    npcConfigs = npcs.mapValues { (_, it) ->
        when (it) {
            is NPC.Hostile -> it.copy(items = emptyList())
            is NPC.Friendly -> it.copy(items = emptyList())
        }
    },
    startItems = items.values.toList(),

    startRoom = "Start room",
    rooms = mapOf(
        "Start room" to Room(
            "Start room",
            "Welcome!",
            npcs = mapOf("georkina" to npcs["georkina"]!!),
            items = items.values.toList(),
            south = "Next room"
        ),
        "Next room" to Room(
            "Next room", "HIIIIIIIIIIIIIIIIIII!",
            npcs = mapOf("geork" to npcs["geork"]!!),
            north = "Start room"
        )
    ),

    master = MASTER,
    onlinePlayers = mapOf(MASTER.ingameName to MASTER),
    allowedUsers = mapOf(MASTER.ingameName to emptyList())
)

val GAME_WITH_PLAYER by lazy {
    GAME_WITHOUT_PLAYER.copy(
        name = "Game with player",
        allowedUsers = GAME_WITHOUT_PLAYER.allowedUsers.plus(
            PLAYER.user.username to listOf(PLAYER.ingameName)
        ),
        onlinePlayers = mapOf(PLAYER.ingameName to PLAYER)
    )
}

fun main() {
    print(Json.decodeFromString<Game>(Json { prettyPrint = true }.encodeToString(GAME_WITHOUT_PLAYER)))
}


fun main2() {
    val config = GAME_WITHOUT_PLAYER.run {
        DungeonConfig(
            name, races, classes, commandConfig, npcConfigs, itemConfigs, startItems, startRoom, rooms
        )
    }
    val json = Json { prettyPrint = true }.encodeToString(config)
    print(json)

}
