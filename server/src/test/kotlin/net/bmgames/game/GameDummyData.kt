package net.bmgames.game

import net.bmgames.authentication.User
import net.bmgames.state.model.*

val master = Player.Master(
    User("master", "master@master.de", "1234", null)
)
val PLAYER by lazy {
    Player.Normal(
        user = User("player1", "", "", null),
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
    name = "Game with only master",
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
    npcConfigs = npcs,
    startItems = items.values.toList(),

    startRoom = "start",
    rooms = mapOf(
        "start" to Room(
            "Start room",
            "Welcome!",
            npcs = mapOf("georkina" to npcs["georkina"]!!),
            items = items.values.toList(),
            south = "room"
        ),
        "room" to Room("Next room", "HIIIIIIIIIIIIIIIIIII!", north = "start")
    ),

    master = master,
    onlinePlayers = mapOf(master.ingameName to master)
)

val GAME_WITH_PLAYER by lazy {
    GAME_WITHOUT_PLAYER.copy(
        "Game with player",
        users = GAME_WITHOUT_PLAYER.users.plus(
            PLAYER.user.username to listOf(PLAYER.ingameName)
        ),
        onlinePlayers = mapOf(PLAYER.ingameName to PLAYER)
    )
}
