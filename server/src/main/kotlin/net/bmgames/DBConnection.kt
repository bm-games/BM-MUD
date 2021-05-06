package net.bmgames

import net.bmgames.database.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction



/**
 * Connects to Database and creates tables if they don't exist
 * */
internal fun ServerConfig.connectToDB() {
    Database.connect(
        driver = "org.postgresql.Driver",
        url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        user = dbUser,
        password = dbPassword
    )
    transaction {
        SchemaUtils.drop()
        SchemaUtils.create(
            UserTable,
            AvatarTable,
            ClassTable,
            GameTable,
            ItemConfigTable,
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
            CommandTable
        )
    }
}

/*
object Games : Table() {
    val config = varchar("config", NAME_LENGTH).references(DungeonConfigs.name)
    val game = varchar("game", NAME_LENGTH).references(GameStates.name)

    override val primaryKey = PrimaryKey(config, game)
}

object DungeonConfigs : Table() {
    val name = varchar("name", NAME_LENGTH)
    val config = json("config", DungeonConfig.serializer())

    override val primaryKey = PrimaryKey(name)
}

object GameStates : Table() {
    val name = varchar("name", NAME_LENGTH)
    val state = json("state", Game.serializer())

    override val primaryKey = PrimaryKey(name)
}

object Players : Table() {
    val name = varchar("name", NAME_LENGTH)
    val game = varchar("game", NAME_LENGTH).references(GameStates.name)
    val user = varchar("user", NAME_LENGTH).references(Users.username)
    val state = json("state", User.serializer())

    override val primaryKey = PrimaryKey(name, game)
}
fun main() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
//        addLogger(StdOutSqlLogger)

        SchemaUtils.create (Users)

        Users.insert {
            it[username] = "Peter"
            it[mail] = "Peter@gmail.com"
            it[passwordHash] = "aaaaaaaa"
            it[mailVerified] = false
        }


        Users.deleteWhere{ Users.username like "%thing"}

        println("All users:")

        for (user in Users.selectAll()) {
            println("${user[Users.username]}: ${user[Users.mail]}")
        }

        SchemaUtils.drop (Users)
    }
}
*/
