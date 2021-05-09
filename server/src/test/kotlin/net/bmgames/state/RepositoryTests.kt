package net.bmgames.state

import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.bmgames.TABLES
import net.bmgames.authentication.User
import net.bmgames.game.GAME_WITHOUT_PLAYER
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.PLAYER
import net.bmgames.game.MASTER
import net.bmgames.state.model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * These tests depend on the ordering
 * */
class RepositoryTests : FunSpec({

    lateinit var player: Player.Normal
    lateinit var masterUser: User
    lateinit var game: Game

    beforeSpec {
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
        game.removeIDs() shouldBe GAME_WITHOUT_PLAYER
    }

    test("Player should be the same after reading and writing") {
        UserRepository.save(PLAYER.user)
        PlayerRepository.savePlayer(
            game,
            PLAYER.copy(user = UserRepository.getUserByName(PLAYER.user.username)!!)
        )
        player = PlayerRepository.loadPlayer(game.name, player.ingameName)!!
        player.removeIDs() shouldBe PLAYER
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
    npcConfigs = npcConfigs.mapValues { (_, npc) ->
        when (npc) {
            is NPC.Friendly -> npc.copy(id = null)
            is NPC.Hostile -> npc.copy(id = null)
        }
    },
    itemConfigs = itemConfigs.mapValues { (_, it) -> it.removeIDs() },
    rooms = rooms.mapValues { (_, it) -> it.copy(id = null) },
    onlinePlayers = onlinePlayers.mapValues { (_, it) ->
        if (it is Player.Normal) it.copy(id = null) else it
    },
    joinRequests = joinRequests.map { it.copy(id = null) }
)

private fun Item.removeIDs() = when (this) {
    is Consumable -> copy(id = null)
    is Equipment -> copy(id = null)
    is Weapon -> copy(id = null)
}
