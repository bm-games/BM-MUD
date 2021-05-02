package net.bmgames.authentication
import io.ktor.sessions.*

/**
 * Authenticator, enacts the creation/registration,login and updates of users which get performed via the userHandler
 *
 * @constructor Create empty Authenticator
 *
 * @property userHandler userHandler to be used to communicate with the database
 */
class Authenticator(val userHandler: UserHandler) {

    /**
     * Register a new user if possible
     *
     * @param mail of the new user
     * @param username of the new user
     * @param password of the new user
     * @return
     */
    fun registerUser(mail: String, username: String, password: String): String? {

        if (userHandler.checkRegisterPossible(mail, username)) {
            val user = User(mail, username, AuthHelper.hashPassword(password))
            userHandler.createUser(user)
        }
        return null
    }


    /**
     * Login user
     *
     * @param mail of the user
     * @param password of the user
     * @return null if user has not verified his Mail or his credentials are wrong.
     * @return Login (Data Class Login) with User and the generated JWT which needs to get assigned.
     */
    fun loginUser(mail: String, password: String): Login? {
        val user = userHandler.getUserByMail(mail)
        if (user != null) {
            if (userHandler.checkMailApproved(user.username)){
                if (user.passwordHash == AuthHelper.hashPassword(password)) {
                    val jwt = AuthHelper.makeToken(user)
                    //TODO("Create JWT and add it via a cookie to the user (ktor)")
                    println("Logged IN")
                    return Login(user, jwt)
                }
            }else{
                println("Verify your mail")
                return null
            }
        }
        println("Wrong Password or No User with this Credentials")
        return null
    }

    /**
     * Starts the process of resetting the user's password based on the provided mail
     *
     * @param mail of the user whose password shall be resetted
     * @return
     */
    fun resetPassword(mail: String): String? {
        userHandler.resetPassword(mail)
        return null
    }

    /**
     * Starts the process to change the user's password by correctly entering the mail and the old password and replacing the old password by the new.
     *
     * @param mail of the user whose password shall be changed
     * @param oldPassword of the user which currently resides in the database
     * @param password new password of the user which shall be updated into the database
     * @return
     */
    fun changePassword(mail: String, oldPassword: String, password: String): String? {
        val user = userHandler.getUserByMail(mail)
        if (user != null && user.passwordHash == AuthHelper.hashPassword(oldPassword)) {
            userHandler.changePassword(mail, AuthHelper.hashPassword(password))
        }
        return null
    }

}
