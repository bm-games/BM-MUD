package net.bmgames

import net.bmgames.state.database.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * All tables that are in use.
 * */
internal val TABLES = arrayOf(
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
    RoomItemTable,
    RoomTable,
    CommandTable,
    JoinRequestTable
)


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
//        Database.connect("jdbc:h2:./testdb", driver = "org.h2.Driver")
    transaction {
//        SchemaUtils.drop(*TABLES)
        SchemaUtils.create(*TABLES)
    }
}
