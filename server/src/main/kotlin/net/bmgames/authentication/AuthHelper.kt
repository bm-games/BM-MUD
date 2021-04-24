package net.bmgames.authentication

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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
        /**
         * Generates a new random password
         *
         *
         */
        fun generatePassword() : String {
            val chars = ('a'..'Z') + ('A'..'Z') + ('0'..'9')
            fun randomToken(): String = List(PASSWORD_LENGTH) { chars.random() }.joinToString("")
            return randomToken()
        }
        /**
         * Hashes the password of the user
         *
         * @Param password of the user
         * @Return hashed version of the user's password
         */
        fun hashPassword(password: String) : String {
            val encrypted = HashingObject.cipher(Cipher.ENCRYPT_MODE, SECRET_KEY).doFinal(password.toByteArray(Charsets.UTF_8))
            return String(HashingObject.encorder.encode(encrypted))
        }
        /**
         * Unhashes the password of the user
         *
         * @Param hashedpassword of the user
         * @return unhashed version of the hashed password
         */
        fun unhashPassword(hashedpassword: String) : String {
            return ""
        }
    }

}