package net.bmgames.state

import net.bmgames.database.*
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object PlayerRepository {

    internal fun createPlayer(game: Game, player: Player.Normal): Unit {
        transaction {
            val gameDAO = GameDAO[game.id]
            val avatarDAO = AvatarDAO.new {
                name = player.avatar.name
                race = RaceDAO[player.avatar.race.id]
                clazz = ClassDAO[player.avatar.clazz.id]
            }
            val visitedRoomDAO = VisitedRoomDAO.new {
                this.game = gameDAO
                room = game.startRoom
            }
            val startItems = game.startItems
                .map { item -> ItemConfigDAO[item.id] }
                .let(::SizedCollection)

            val playerDAO = PlayerDAO.new {
                this.game = gameDAO
                user = UserDAO[player.user.id]
                avatar = avatarDAO
                room = game.startRoom
                health = avatar.race.health
                inventory = startItems
                visitedRooms = SizedCollection(visitedRoomDAO)
            }
            visitedRoomDAO.player = playerDAO
        }
    }

    internal fun savePlayer(gameName: String, player: Player.Normal): Unit {
//        TODO("Not yet implemented")
    }
}

