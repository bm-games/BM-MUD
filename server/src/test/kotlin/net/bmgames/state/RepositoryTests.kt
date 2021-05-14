package net.bmgames.state

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import net.bmgames.TABLES
import net.bmgames.authentication.User
import net.bmgames.game.GAME_WITHOUT_PLAYER
import net.bmgames.game.PLAYER
import net.bmgames.game.MASTER
import net.bmgames.game.ITEMS
import net.bmgames.state.database.PlayerDAO
import net.bmgames.state.database.PlayerTable
import net.bmgames.state.model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * These tests depend on the ordering
 * */
class RepositoryTests : FunSpec({

    lateinit var player: Player.Normal
    lateinit var masterUser: User
    lateinit var game: Game

    beforeSpec {
//        Database.connect("jdbc:h2:./testdb;DB_CLOSE_DELAY=2;", driver = "org.h2.Driver")
//        transaction { SchemaUtils.drop(*TABLES); SchemaUtils.create(*TABLES) }
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction { SchemaUtils.create(*TABLES) }
    }

    test("User should be the same after reading and writing") {
        UserRepository.save(MASTER.user)
        masterUser = UserRepository.getUserByName(MASTER.user.username)!!
        masterUser.copy(id = null) shouldBe MASTER.user
    }

    test("Game should be the same after reading and writing") {
        val gme = GAME_WITHOUT_PLAYER.copy(master = Player.Master(masterUser))
        GameRepository.save(gme)
        game = GameRepository.loadGame(gme.name)!!
        game.removeIDs() shouldBe GAME_WITHOUT_PLAYER.copy(onlinePlayers = emptyMap())
    }

    test("Player should be the same after reading and writing") {
        UserRepository.save(PLAYER.user)
        val newPlayer = PLAYER.copy(
            user = UserRepository.getUserByName(PLAYER.user.username)!!,
            inventory = Inventory(
                game.itemConfigs["Wooden Sword"]!! as Weapon,
                mapOf(Equipment.Slot.Boots to game.itemConfigs["Diamond Shoes"]!! as Equipment),
                listOf(game.itemConfigs["Apfel"]!!)
            ),
        )
        PlayerRepository.savePlayer(game, newPlayer)
        player = PlayerRepository.loadPlayer(game.name, PLAYER.ingameName)!!
        player.removeIDs() shouldBe newPlayer.removeIDs()
    }

    test("Updating the game state should work correctly") {
        val newGame = game.copy(
            rooms = game.rooms.plus("New room" to Room(
                "New room",
                "Hi",
                "Next room",
                items = game.startItems,
                npcs = game.getStartRoom().npcs
                    .mapValues { (_, npc) -> (npc as NPC.Hostile).copy(items = game.startItems, id = null) }
            )),
            allowedUsers = game.allowedUsers.plus(player.user.username to setOf(player.ingameName))
        )
        GameRepository.save(newGame)
        game = GameRepository.loadGame(newGame.name)!!
        game.removeIDs() shouldBe newGame.removeIDs()
    }

    test("Deleting the game state should remove the game and all players") {
        GameRepository.delete(game)
        GameRepository.loadGame(game.name).shouldBeNull()

        transaction {
            PlayerDAO.find { PlayerTable.game eq game.id } shouldHaveSize 0
        }
    }
})

private fun Player.Normal.removeIDs(): Player.Normal = copy(
    id = null,
    user = user.copy(id = null),
    avatar = avatar.removeIDs(),
    inventory = inventory.removeIDs(),
)

private fun Inventory.removeIDs(): Inventory = copy(
    weapon = weapon?.removeIDs() as Weapon?,
    items = items.map { it.removeIDs() },
    equipment = equipment.mapValues { (_, it) -> it.removeIDs() as Equipment }
)

private fun Avatar.removeIDs(): Avatar = copy(
    id = null,
    race = race.copy(id = null),
    clazz = clazz.copy(id = null),
)

private fun Game.removeIDs() = copy(
    id = null,
    races = races.map { it.copy(id = null) },
    classes = classes.map { it.copy(id = null) },
    startItems = startItems.map { it.removeIDs() },
    npcConfigs = npcConfigs.mapValues { (_, npc) -> npc.removeIDs() },
    itemConfigs = itemConfigs.mapValues { (_, it) -> it.removeIDs() },
    rooms = rooms.mapValues { (_, it) ->
        it.copy(id = null,
            npcs = it.npcs.mapValues { (_, npc) -> npc.removeIDs() },
            items = it.items.map { it.removeIDs() }
        )
    },
    onlinePlayers = onlinePlayers.mapValues { (_, it) ->
        if (it is Player.Normal) it.copy(id = null) else it
    },
    master = Player.Master(master.user.copy(id = null)),
    joinRequests = joinRequests.map { it.copy(id = null) }
)

private fun NPC.removeIDs() = when (this) {
    is NPC.Friendly -> copy(id = null, items = items.map { it.removeIDs() })
    is NPC.Hostile -> copy(id = null, items = items.map { it.removeIDs() })
}

private fun Item.removeIDs() = when (this) {
    is Consumable -> copy(id = null)
    is Equipment -> copy(id = null)
    is Weapon -> copy(id = null)
}
