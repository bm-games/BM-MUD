package net.bmgames.authentication
const val KEY_LENGTH = 10
const val PASSWORD_LENGTH = 20
class AuthHelper {

    companion object{
        fun generateVerificationToken(user: User) {
            val chars = ('a'..'Z') + ('A'..'Z') + ('0'..'9')
            fun randomToken(): String = List(KEY_LENGTH) { chars.random() }.joinToString("")
            val token = randomToken()
            user.registrationKey = token
        }

        fun generatePassword() : String {
            val chars = ('a'..'Z') + ('A'..'Z') + ('0'..'9')
            fun randomToken(): String = List(PASSWORD_LENGTH) { chars.random() }.joinToString("")
            return randomToken()
        }

        fun hashPassword(password: String) : String {
            return ""
        }

        fun unhashPassword(hashedpassword: String) : String {
            return ""
        }
    }

}