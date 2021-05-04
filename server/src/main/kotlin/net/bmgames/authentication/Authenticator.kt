package net.bmgames.authentication

import arrow.core.Either
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.sessions.*
import net.bmgames.ErrorMessage
import net.bmgames.error
import net.bmgames.success


/**
 * Authenticator, enacts the creation/registration,login and updates of users which get performed via the userHandler
 *
 * @constructor Create empty Authenticator
 *
 * @property userHandler userHandler to be used to communicate with the database
 */
class Authenticator(
    private val authHelper: AuthHelper,
    private val userHandler: UserHandler
) {

    /**
     * Register a new user if possible
     *
     * @param mail of the new user
     * @param username of the new user
     * @param password of the new user
     * @return
     */
    fun registerUser(mail: String, username: String, password: String): Either<ErrorMessage, Unit> {
        return if (userHandler.checkRegisterPossible(mail, username)) {
            val user = User(mail, username, authHelper.hashPassword(password), null)
            userHandler.createUser(user)
            success
        } else {
            error("A user with this email or password already exists.")
        }
    }


    /**
     * Login user
     *
     * @param mail of the user
     * @param password of the user
     * @return null if user has not verified his Mail or his credentials are wrong.
     * @return Login (Data Class Login) with User and the generated JWT which needs to get assigned.
     */
    fun loginUser(mail: String, password: String): Either<ErrorMessage, User> {
        val user = userHandler.getUserByMail(mail)
        if (user != null) {
            if (userHandler.checkMailApproved(user.username)) {
                if (user.passwordHash == authHelper.hashPassword(password)) {
                    return success(user)
                }
            } else {
                return error("Mail not verified.")
            }
        }
        return error("Wrong password or no user with this credentials.")
    }

    /**
     * Starts the process of resetting the user's password based on the provided mail
     *
     * @param mail of the user whose password shall be resetted
     * @return
     */
    fun resetPassword(mail: String): Unit {
        userHandler.resetPassword(mail)
    }

    /**
     * Starts the process to change the user's password by correctly entering the mail and the old password and
     * replacing the old password by the new.
     *
     * @param mail of the user whose password shall be changed
     * @param oldPassword of the user which currently resides in the database
     * @param password new password of the user which shall be updated into the database
     * @return
     */
    fun changePassword(user: User, oldPassword: String, password: String): Either<ErrorMessage, Unit> {
        return if (user.passwordHash == authHelper.hashPassword(oldPassword)) {
            userHandler.changePassword(user.email, authHelper.hashPassword(password))
            success
        } else {
            error("Password is incorrect.")
        }
    }

    fun verifyToken(token: String) = userHandler.setMailApproved(token)

}

fun ApplicationCall.getUser(): User? = sessions.get<User>()

suspend inline fun ApplicationCall.withUser(block: User.() -> Unit): Unit =
    sessions.get<User>()?.let(block) ?: respond(HttpStatusCode.Forbidden)

