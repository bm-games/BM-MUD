package net.bmgames.authentication

import net.bmgames.Communication.MailNotifier

var currentUser: net.bmgames.authentication.User? = null;

class Authenticator {
   companion object{
       val userHandler: UserHandler = UserHandler()
       val mailNotifier: MailNotifier = MailNotifier()
   }
    /**
     *
     *
     */

    //Basis zur morgigen Arbeit
    fun registerUser(mail: String, username: String, password: String) : String?{

        if(userHandler.checkRegisterPossible(mail, username)){
            val user = User(mail, username, AuthHelper.hashPassword(password), false)
            userHandler.createUser(user)
        }
        return null
    }


    /**
     *
     *
     */
    fun loginUser(mail: String, password: String) : String?{
        val user = userHandler.getUserByMail(mail)
        if(user != null)
        {
            if(user.passwordHash == AuthHelper.hashPassword(password))
            {
                currentUser = user
                return null
            }
        }
            return "Fail"
    }

    fun resetPassword(mail: String) :String?{
        userHandler.resetPassword(mail)
        return null
    }

    fun changePassword(mail: String, oldPassword: String, password: String) :String? {
        val user = userHandler.getUserByMail(mail)
        if(user != null && user.passwordHash == AuthHelper.hashPassword(oldPassword)) {
            userHandler.changePassword(mail, AuthHelper.hashPassword(password))
        }
        return null
    }

}