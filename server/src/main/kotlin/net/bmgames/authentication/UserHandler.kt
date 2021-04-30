package net.bmgames.authentication

import net.bmgames.communication.MailNotifier
import net.bmgames.database.UserTable
import net.bmgames.database.VerificationTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

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
                        passwordHash = it[UserTable.passwordHash]
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
                        passwordHash = it[UserTable.passwordHash]
                    )
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
    internal fun createUser(user: User) {
        var isExceptionPresent: Boolean
        var token: String = "None"
        /*
        If the VerificationToken/RegistrationKey already exists in the database,
        an SQLException is thrown. If this is the case, a token is generated again
        and the process runs again.
        (Probability for this is small, but exists!).
         */
        do {
            try {
                token = AuthHelper.generateVerificationToken()
                transaction {
                    VerificationTable.insert {
                        it[registrationKey] = token
                        it[username] = user.username
                        it[mailVerified] = false
                    }
                }
                isExceptionPresent = false
            } catch (e: SQLException){
                isExceptionPresent = true
            }
        } while (isExceptionPresent)
        transaction {
            UserTable.insert {
                it[email] = user.email
                it[username] = user.username
                it[passwordHash] = user.passwordHash
            }
        }
        mailNotifier.sendMailRegister(user, token)
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
     * @param token Verification Token of the User which gets Approved
     */
    internal fun setMailApproved(token: String) {
        transaction {
            VerificationTable.update({ VerificationTable.registrationKey eq token }) {
                it[mailVerified] = true
            }
        }
    }

    /**
     * Checks if the User has already verified his mail
     *
     * @param username of the User which gets checked
     * @return A Boolean Value (True = mail is Verified, False = Mail is not Verified)
     */
    internal fun checkMailApproved(username: String): Boolean {
        val approved = transaction {
            VerificationTable.select{
                VerificationTable.username eq username
            }.withDistinct().map {
                it[VerificationTable.mailVerified]
            }

        }
        return approved[0]
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
            return false
        return true
    }
}
