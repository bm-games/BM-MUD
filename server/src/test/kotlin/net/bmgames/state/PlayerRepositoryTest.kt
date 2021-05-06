package net.bmgames.state

import io.kotest.core.spec.style.FunSpec
import net.bmgames.database.*
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.PLAYER
import net.bmgames.game.master
import net.bmgames.state.model.setId
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerRepositoryTest : FunSpec({
})

fun main() {
    Database.connect("jdbc:h2:./testdb", driver = "org.h2.Driver")
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.drop(
            UserTable,
            AvatarTable,
            ClassTable,
            GameTable,
            ItemConfigTable,
            StartItemTable,
            NPCConfigTable,
            NPCItemTable,
            NPCTable,
            PlayerItemTable,
            PlayerTable,
            RaceTable,
            RoomConfigTable,
            RoomItemTable,
            RoomTable,
            VisitedRoomsTable,
            CommandTable,
            JoinRequestTable
        )
        SchemaUtils.create(
            UserTable,
            AvatarTable,
            ClassTable,
            GameTable,
            ItemConfigTable,
            StartItemTable,
            NPCConfigTable,
            NPCItemTable,
            NPCTable,
            PlayerItemTable,
            PlayerTable,
            RaceTable,
            RoomConfigTable,
            RoomItemTable,
            RoomTable,
            VisitedRoomsTable,
            CommandTable,
            JoinRequestTable
        )
    }
    transaction {
        val userDAO = UserDAO.new {
            username = master.user.username
            email = master.user.email
            passwordHash = master.user.passwordHash
            registrationKey = master.user.registrationKey
        }
        master.user.setId(userDAO.id.value)
    }
    GameRepository.create(GAME_WITH_PLAYER)
    val game = GameRepository.loadGame(GAME_WITH_PLAYER.name)
    println(game)
//    PlayerRepository.createPlayer(GAME_WITH_PLAYER, PLAYER)
//    val loadPlayer = PlayerRepository.loadPlayer(GAME_WITH_PLAYER.name, PLAYER.ingameName)
}
