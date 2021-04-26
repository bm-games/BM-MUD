package net.bmgames.authentication


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
            val user = User(mail, username, AuthHelper.hashPassword(password), false)
            userHandler.createUser(user)
        }
        return null
    }


    /**
     * Login user
     *
     * @param mail of the user
     * @param password of the user
     * @return
     */
    fun loginUser(mail: String, password: String): String? {
        val user = userHandler.getUserByMail(mail)
        if (user != null) {
            if (user.passwordHash == AuthHelper.hashPassword(password)) {
                TODO("Create JWT and add it via a cookie to the user (ktor)")
            }
        }
        return "Fail"
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
