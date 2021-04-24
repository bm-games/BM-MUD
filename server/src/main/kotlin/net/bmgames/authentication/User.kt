package net.bmgames.authentication

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object usertable/*: IdTable<String>()*/ :Table() {
    val email = varchar("email", 100)
    val username = varchar("username", 100)
    val passwordHash = varchar("passwordHash", 100)
    val mailVerified = bool("mailVerified")

    override val primaryKey = PrimaryKey(username, name = "username")
    //override val id: Column<EntityID<String>> = username.entityId()
}
/**
 * @param email Identifies user uniquely.
 * @param username Identifies user uniquely.
 * @param passwordHash Hashed User Password.
 * @param mailVerified Boolean value which determines whether a user has confirmed his e-mail or not.
 * @param registrationKey Is only assigned when User maiLVerified is not true and value is used to verify this user.
 * */
class User(
    val email: String,
    val username: String,
    val passwordHash: String,
    val mailVerified: Boolean,
    var registrationKey: String = ""
) /*:Entity<String>(EntityID(username, UserTable))*/ {
    /*companion object : EntityClass<String, User>(UserTable)*/
}