package net.bmgames.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import net.bmgames.ServerConfig
import net.bmgames.message
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val KEY_LENGTH = 10
const val PASSWORD_LENGTH = 20

/**
 * Auth helper
 *
 * @Constructor Create empty Auth helper
 */
class AuthHelper(val config: ServerConfig) {

    /*companion object ChCrypto { TODO Wozu?
        fun aesEncrypt(v: String) = hashPassword(v)
        fun aesDecrypt(v: String) = unhashPassword(v)
    }*/

    /**
     * HashingObject
     *
     * Assisting object to and unhash the passwords of the users.
     */
    object HashingObject {
        val encorder = Base64.getEncoder()
        val decorder = Base64.getDecoder()
        fun cipher(opmode: Int, secretKey: String): Cipher {
            if (secretKey.length != 32) throw SecurityException(message("auth.secret-key-length"))
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
     *
     */
    fun generateVerificationToken(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        fun randomToken(): String = List(KEY_LENGTH) { chars.random() }.joinToString("")
        val token = randomToken()

        return token

    }

    /**
     * Generates a new random password
     *
     *
     */
    fun generatePassword(): String {
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
    fun hashPassword(password: String): String {
        val encrypted =
            HashingObject.cipher(Cipher.ENCRYPT_MODE, config.secretKeyHash)
                .doFinal(password.toByteArray(Charsets.UTF_8))
        return String(HashingObject.encorder.encode(encrypted))
    }

    /**
     * Unhashes the password of the user
     *
     * @Param hashedpassword of the user
     * @return unhashed version of the hashed password
     */
    fun unhashPassword(hashedpassword: String): String {
        val byteStr = HashingObject.decorder.decode(hashedpassword.toByteArray(Charsets.UTF_8))
        return String(HashingObject.cipher(Cipher.DECRYPT_MODE, config.secretKeyHash).doFinal(byteStr))
    }

    /**
     * Everything for Token Generation and Stuff Like that
     *
     *
     */
    private val secret = "R6A6QCo5oTygj37aM4zG" //replace with a secret Key from config.json
    private val issuer = "bm-games.net"   //replace with a value from config.json
    private val audience = "bm-games-audience"    //replace with a value from config.json
    private val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(user: User?): String = JWT.create()
        .withSubject("AuthToken")
        .withIssuer(issuer)
        .withClaim("username", user?.username)
        .withClaim("mail", user?.email)
        .withClaim("pw", user?.passwordHash)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

}
