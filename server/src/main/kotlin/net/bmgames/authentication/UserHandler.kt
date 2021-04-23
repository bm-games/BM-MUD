package net.bmgames.authentication

import net.bmgames.Communication.MailNotifier

class UserHandler {

   /* protected fun getUser(token: JWT): User?{
        return null
    }*/
    val mailNotifier: MailNotifier = MailNotifier()
    var currentUser: User? = null

    fun getUserByName(username: String): User?{
        return null
    }
    fun getUserByMail(mail: String): User?{
        return null
    }

    public fun createUser(user: User) {
        if (checkRegisterPossible(user.email, user.username)) {
            AuthHelper.generateVerificationToken(user)
            //WriteOnDB /tbd
            mailNotifier.sendMailRegister(user)
        }
    }

    fun resetPassword(mail: String){
        val userByMail = getUserByMail(mail)
        if (userByMail != null)
        {
            val password = AuthHelper.generatePassword()
            changePassword(mail, password)
            mailNotifier.sendMailReset(userByMail)
        }
    }

    fun changePassword(mail: String, password: String){
        //DBZugriff

    }

    public fun setMailApproved(mail: String){

    }

    public fun checkRegisterPossible(email: String, username: String) : Boolean{
        return false
    }
}