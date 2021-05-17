package net.bmgames.game

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmgames.authentication.User
import net.bmgames.communication.Notifier
import net.bmgames.state.DungeonConfig
import net.bmgames.state.model.*
import net.bmgames.state.model.Direction.NORTH
import net.bmgames.state.model.Direction.SOUTH


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
        inventory = Inventory(),
        room = GAME_WITHOUT_PLAYER.startRoom,
        healthPoints = 10,
        lastHit = null,
        visitedRooms = hashSetOf(GAME_WITHOUT_PLAYER.startRoom)
    )
}

val ITEMS = listOf(
    Consumable("Apfel", "heal \$player 10"),
    Equipment("Diamond Helmet", 10f, 1f, Equipment.Slot.Head),
    Equipment("Diamond Chest", 10f, 1f, Equipment.Slot.Chest),
    Equipment("Diamond Legs", 10f, 1f, Equipment.Slot.Legs),
    Equipment("Diamond Shoes", 10f, 1f, Equipment.Slot.Boots),
    Weapon("Wooden Sword", 1),
).associateBy { it.name }

val npcs = mapOf(
    "geork" to NPC.Friendly("geork", listOf(ITEMS["Apfel"]!!), "heal \$player 1", "Halloooooooooo"),
    "georkina" to NPC.Hostile("georkina", listOf(ITEMS["Diamond Helmet"]!!), 100, 100000),
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
    commandConfig = CommandConfig(
        aliases = mapOf("say" to "sagen"),
        customCommands = mapOf("karussel" to "move north\nmove west\nmove south\nmove east"),
    ),
    itemConfigs = ITEMS,
    npcConfigs = npcs.mapValues { (_, it) ->
        when (it) {
            is NPC.Hostile -> it.copy(items = emptyList())
            is NPC.Friendly -> it.copy(items = emptyList())
        }
    },
    startItems = ITEMS.values.toList(),

    startRoom = "Start room",
    rooms = mapOf(
        "Start room" to Room(
            "Start room",
            "Welcome!",
            npcs = mapOf("georkina" to npcs["georkina"]!!),
            items = ITEMS.values.toList(),
            neighbours = mapOf(SOUTH to "Next room")
        ),
        "Next room" to Room(
            "Next room", "HIIIIIIIIIIIIIIIIIII!",
            npcs = mapOf("geork" to npcs["geork"]!!),
            neighbours = mapOf(NORTH to "Start room")
        )
    ),

    master = MASTER,
    onlinePlayers = mapOf(MASTER.ingameName to MASTER),
    allowedUsers = mapOf(MASTER.ingameName to emptySet())
)

val GAME_WITH_PLAYER by lazy {
    GAME_WITHOUT_PLAYER.copy(
        name = "Game with player",
        allowedUsers = GAME_WITHOUT_PLAYER.allowedUsers.plus(
            PLAYER.user.username to setOf(PLAYER.ingameName)
        ),
        onlinePlayers = GAME_WITHOUT_PLAYER.onlinePlayers + (PLAYER.ingameName to PLAYER)
    )
}

val NOOP_NOTIFIER = object : Notifier {
    override fun send(recipient: User, subject: String, message: String) {}
}

fun main() {
    print(Json { prettyPrint = true }.encodeToString(GAME_WITHOUT_PLAYER))
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
