package net.bmgames.state

import net.bmgames.authentication.User
import net.bmgames.database.UserDAO
import net.bmgames.database.UserTable
import net.bmgames.database.updateOrCreate
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    /**
     * Saves or updates the user
     * */
    fun save(user: User): Unit {
        transaction {
            UserDAO.updateOrCreate(user.id) {
                username = user.username
                email = user.email
                passwordHash = user.passwordHash
                registrationKey = user.registrationKey
            }
        }
    }


    /**
     * Gets the user from the database based on the username
     *
     * @param username Identifies user uniquely.
     * @return an object of the class User
     */
    fun getUserByName(username: String): User? = transaction {
        UserDAO.find { UserTable.username eq username }.firstOrNull()?.toUser()
    }

    /**
     * Gets the user from the database based on the email
     *
     * @param mail Identifies user uniquely.
     * @return an object of the class User
     */
    fun getUserByMail(mail: String): User? = transaction {
        UserDAO.find { UserTable.email eq mail }.firstOrNull()?.toUser()
    }
}
