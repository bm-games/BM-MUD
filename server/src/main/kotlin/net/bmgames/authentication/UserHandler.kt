package net.bmgames.authentication

import arrow.core.Either
import net.bmgames.ErrorMessage
import net.bmgames.communication.MailNotifier
import net.bmgames.state.database.UserTable
import net.bmgames.errorMsg
import net.bmgames.state.UserRepository
import net.bmgames.state.UserRepository.getUserByMail
import net.bmgames.state.UserRepository.getUserByName
import net.bmgames.success
import org.jetbrains.exposed.sql.or
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
     * Creates a user on the database from an user object
     * Also adding the Verification Token to the Database with its other Informations
     * ->Could be the case that the generated Token already exists in the DB
     * ->Implemented ExceptionHandler for that Case
     *
     * @param user
     */
    internal fun createUser(user: User): Boolean {
        val token = user.username + authHelper.generateVerificationToken()
        return try {
            UserRepository.save(user.copy(registrationKey = token))
            mailNotifier.sendMailRegister(user, token)
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
    internal fun setMailApproved(token: String): Either<ErrorMessage, Unit> = transaction {
        if (UserTable.select { UserTable.registrationKey eq token }.none()) {
            errorMsg("Token not valid.")
        } else {
            UserTable.update({ UserTable.registrationKey eq token }) {
                it[registrationKey] = null
            }
            success
        }
    }

    /**
     * Checks if the User has already verified his mail
     *
     * @param username of the User which gets checked
     * @return A Boolean Value (True = mail is Verified, False = Mail is not Verified)
     */
    internal fun checkMailApproved(username: String): Boolean {
        return getUserByName(username)?.isMailVerified() ?: false

    }

    /**
     * Checks based on the email-address and/or username if an user can be created with those values
     *
     * @param email email to check if there is a user with this mail
     * @param username username to check if there is a user with this username
     * @return
     */
    internal fun checkRegisterPossible(email: String, username: String): Boolean {
        return transaction {
            UserTable.select {
                (UserTable.username eq username) or (UserTable.email eq email)
            }.none()
        }
    }
}
