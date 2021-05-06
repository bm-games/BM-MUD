package net.bmgames.authentication

import arrow.core.Either
import arrow.core.Nullable
import arrow.core.Option.Companion.catch
import net.bmgames.ErrorMessage
import net.bmgames.communication.MailNotifier
import net.bmgames.database.UserDAO
import net.bmgames.database.UserTable
import net.bmgames.error
import net.bmgames.success
import net.bmgames.tryOrNull
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/**
 * @property mailNotifier Needed for sending registration / password reset mails
 */
class UserHandler(
    private val mailNotifier: MailNotifier,
    private val authHelper: AuthHelper
) {


    /**
     * Gets the user from the database based on the username
     *
     * @param username Identifies user uniquely.
     * @return an object of the class User
     */
    fun getUserByName(username: String): User? {
        return transaction {
            UserDAO.find { UserTable.username eq username }.firstOrNull()
                ?.run {
                    User(email, username, passwordHash, registrationKey)
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
            UserDAO.find { UserTable.email eq mail }.firstOrNull()
                ?.run {
                    User(email, username, passwordHash, registrationKey)
                }
        }
    }

    /**
     * Creates a user on the database from an user object
     * Also adding the Verification Token to the Database with its other Informations
     * ->Could be the case that the generated Token already exists in the DB
     * ->Implemented ExceptionHandler for that Case
     *
     * @param user
     */
    internal fun createUser(user: User): Boolean {
        val token = authHelper.generateVerificationToken()
        return try {
            transaction {
               UserDAO.new {
                   username = user.username
                   email = user.email
                   passwordHash = user.passwordHash
                   registrationKey = user.registrationKey
               }
            }
            mailNotifier.sendMailRegister(user, "${user.username}$token")
            true
        } catch (_: Exception) {
            false
        }
    }


    /**
     * Resets password of an account with the provided mail
     *
     * @param mail
     */
    internal fun resetPassword(mail: String) {
        val userByMail = getUserByMail(mail)
        if (userByMail != null) {
            val password = authHelper.generatePassword()
            changePassword(mail, authHelper.hashPassword(password))
            mailNotifier.sendMailReset(userByMail, password)
        }

    }

    /**
     * Changes the password of a user using the supplied password and mail
     *
     * @param mail mail of the account which password is to be changed
     * @param hashedPassword new password for the user
     */
    internal fun changePassword(mail: String, hashedPassword: String) {
        transaction {
            UserTable.update({ UserTable.email eq mail }) {
                it[passwordHash] = hashedPassword
            }
        }

    }

    /**
     * Set mail approved
     *
     * @param token Verification Token of the User which gets Approved
     */
    internal fun setMailApproved(token: String): Either<ErrorMessage, Unit> {
        val rowsUpdated = transaction {
            UserTable.update({ UserTable.registrationKey eq token }) {
                it[registrationKey] = null
            }
        }
        return if (rowsUpdated == 1) {
            success
        } else {
            error("Token not valid.")
        }
    }

    /**
     * Checks if the User has already verified his mail
     *
     * @param username of the User which gets checked
     * @return A Boolean Value (True = mail is Verified, False = Mail is not Verified)
     */
    internal fun checkMailApproved(username: String): Boolean {
        val status = getUserByName(username)
        return status?.isMailVerified() ?: false

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
        return userByMail == null && userByName == null
    }
}
