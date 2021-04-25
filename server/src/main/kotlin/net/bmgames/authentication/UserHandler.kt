package net.bmgames.authentication

import net.bmgames.communication.MailNotifier
import net.bmgames.database.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @property mailNotifier Needed for sending registration / password reset mails
 */
class UserHandler(private val mailNotifier: MailNotifier) {


    /* protected fun getUser(token: JWT): User?{
        return null
    }*/


    /**
     * Gets the user from the database based on the username
     *
     * @param username Identifies user uniquely.
     * @return an object of the class User
     */
    fun getUserByName(username: String): User? {
        return transaction {
            UserTable.select {
                UserTable.username eq username
            }.firstOrNull()
                ?.let {
                    User(
                        email = it[UserTable.email],
                        username = it[UserTable.username],
                        passwordHash = it[UserTable.passwordHash],
                        mailVerified = it[UserTable.mailVerified],
                        registrationKey = ""
                    )
                }
        }
    }

    /**
     * Gets the user from the database based on the email
     *
     * @param mail Identifies user uniquely.
     * @return an object of the class User
     */
    fun getUserByMail(mail: String): User? {
        return transaction {
            UserTable.select {
                UserTable.email eq mail
            }.firstOrNull()
                ?.let {
                    User(
                        email = it[UserTable.email],
                        username = it[UserTable.username],
                        passwordHash = it[UserTable.passwordHash],
                        mailVerified = it[UserTable.mailVerified],
                        registrationKey = ""
                    )
                }
        }
    }

    /**
     * Creates a user on the database from an user object
     *
     * @param user
     */
    internal fun createUser(user: User) {
        AuthHelper.generateVerificationToken(user)
        transaction {
            UserTable.insert {
                it[email] = user.email
                it[username] = user.username
                it[passwordHash] = user.passwordHash
                it[mailVerified] = user.mailVerified
            }
        }
        mailNotifier.sendMailRegister(user)
    }


    /**
     * Resets password of an account with the provided mail
     *
     * @param mail
     */
    internal fun resetPassword(mail: String) {
        val userByMail = getUserByMail(mail)
        if (userByMail != null) {
            val password = AuthHelper.hashPassword(AuthHelper.generatePassword())
            changePassword(mail, password)
            mailNotifier.sendMailReset(userByMail)
        }

    }

    /**
     * Changes the password of a user using the supplied password and mail
     *
     * @param mail mail of the account which password is to be changed
     * @param password new password for the user
     */
    internal fun changePassword(mail: String, password: String) {
        transaction {
            UserTable.update({ UserTable.email eq mail }) {
                it[passwordHash] = password
            }
        }

    }

    /**
     * Set mail approved
     *
     * @param mail mail-address of the user which gets approved
     */
    internal fun setMailApproved(mail: String) {
        transaction {
            UserTable.update({ UserTable.email eq mail }) {
                it[mailVerified] = true
            }
        }
    }

    /**
     * Checks based on the email-address and/or username if an user can be created with those values
     *
     * @param email email to check if there is a user with this mail
     * @param username username to check if there is a user with this username
     * @return
     */
    internal fun checkRegisterPossible(email: String, username: String): Boolean {
        val userByMail = getUserByMail(email)
        val userByName = getUserByName(username)
        if (userByMail != null || userByName != null)
            return true
        return false
    }
}
