package net.bmgames.state

import net.bmgames.state.database.*
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Normal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.ConcurrentHashMap

object PlayerRepository {

    private val cache: ConcurrentHashMap<Pair<String, String>, Normal?> =
        ConcurrentHashMap<Pair<String, String>, Normal?>()

    /**
     * Reads a player from the db
     * */
    internal fun loadPlayer(gameName: String, ingameName: String): Normal? =
        cache.computeIfAbsent(gameName to ingameName) {
            transaction {
                val query = PlayerTable.innerJoin(GameTable).innerJoin(AvatarTable)
                    .slice(PlayerTable.columns)
                    .select { GameTable.name eq gameName and (AvatarTable.name eq ingameName) }
                    .withDistinct()

                PlayerDAO.wrapRows(query).firstOrNull()?.toPlayer()
            }
        }

    /**
     * Updates or creates the player in a game
     * @param game The game of the player. If it doesn't exist in the db it will get created here.
     * @param player The Player to save. The User of the player must exist in the db
     * @throws NullPointerException If a precondition is not met
     * @throws SqlException //TODO
     * */
    internal fun savePlayer(game: Game, player: Normal): Unit {
        cache[game.name to player.ingameName] = player

        val playerDAO = transaction {
            if (game.id == null) GameRepository.save(game)
            val gameDAO = GameDAO[game.id!!]
            val userDAO = UserDAO[player.user.id!!]

            val avatarDAO = AvatarDAO.getOrCreate(player.avatar.id) {
                name = player.avatar.name
                race = gameDAO.races.find { it.name == player.avatar.race.name }!!
                clazz = gameDAO.classes.find { it.name == player.avatar.clazz.name }!!
            }

            PlayerDAO.updateOrCreate(player.id) {
                gameRef = gameDAO
                avatar = avatarDAO
                user = userDAO
                room = player.room
                health = player.healthPoints
                visitedRooms = player.visitedRooms
            }
        }

        transaction {
            playerDAO.inventory = (player.inventory.items + player.inventory.equipment.values + player.inventory.weapon)
                .mapNotNull { item -> item?.id?.let { ItemConfigDAO[it] } }
                .toSized()
        }
    }

}

