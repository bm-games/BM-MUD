package net.bmgames.authentication

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val KEY_LENGTH = 10
const val PASSWORD_LENGTH = 20
const val SECRET_KEY = "YYiSB7kY5Ed5mttaJRSkgHEPF43iLjTA"

/**
 * Auth helper
 *
 * @Constructor Create empty Auth helper
 */
class AuthHelper {


    companion object{
        object ChCrypto{
            @JvmStatic fun aesEncrypt(v:String) = AuthHelper.hashPassword(v)
            @JvmStatic fun aesDecrypt(v:String) = AuthHelper.unhashPassword(v)
        }
        /**
         * HashingObject
         *
         * Assisting object to and unhash the passwords of the users.
         */
        public object HashingObject {
            public val encorder = Base64.getEncoder()
            public val decorder = Base64.getDecoder()
            public fun cipher(opmode: Int, secretKey: String): Cipher {
                if (secretKey.length != 32) throw RuntimeException("SecretKey length is not 32 chars")
                val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
                val sk = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
                val iv = IvParameterSpec(secretKey.substring(0, 16).toByteArray(Charsets.UTF_8))
                c.init(opmode, sk, iv)
                return c
            }
        }
        /**
         * Generates a Token to verify the user
         *
         * @Param user user whose account shall be verified
         */
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
            val byteStr = HashingObject.decorder.decode(hashedpassword.toByteArray(Charsets.UTF_8))
            return String(HashingObject.cipher(Cipher.DECRYPT_MODE, SECRET_KEY).doFinal(byteStr))
        }
    }

}
