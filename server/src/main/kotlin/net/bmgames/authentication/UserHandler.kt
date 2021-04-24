package net.bmgames.authentication

import net.bmgames.Communication.MailNotifier
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * User handler
 *
 * @constructor Create empty User handler
 *
 * @property mailNotifier always gets instantiated with an object of the MailNotifier class to be able to send mails
 *
 */
public class UserHandler {


    /* protected fun getUser(token: JWT): User?{
        return null
    }*/

        val mailNotifier: MailNotifier = MailNotifier()
        var currentUser: User? = null

    /**
     * Gets the user from the database based on the username
     *
     * @param username Identifies user uniquely.
     * @return an object of the class User
     */
    fun getUserByName(username: String): User? {
            var returnedUser: User? = null
            transaction(/*später DB von bigBoyJohannes*/) {
                usertable.select() {
                    usertable.username eq username
                }.forEach {
                    returnedUser = User(
                        email = it[usertable.email],
                        username = it[usertable.username],
                        passwordHash = it[usertable.passwordHash],
                        mailVerified = it[usertable.mailVerified],
                        registrationKey = ""
                    )
                }
            }
            return returnedUser
        }

    /**
     * Gets the user from the database based on the email
     *
     * @param mail Identifies user uniquely.
     * @return an object of the class User
     */
    fun getUserByMail(mail: String): User? {
            var returnedUser: User? = null
            transaction(/*später DB von bigBoyJohannes*/) {
                usertable.select() {
                    usertable.email eq mail
                }.forEach {
                    returnedUser = User(
                        email = it[usertable.email],
                        username = it[usertable.username],
                        passwordHash = it[usertable.passwordHash],
                        mailVerified = it[usertable.mailVerified],
                        registrationKey = ""
                    )
                }
            }
            return returnedUser
        }

    /**
     * Creates a user on the database from an user object
     *
     * @param user
     */
    public fun createUser(user: User) {
            AuthHelper.generateVerificationToken(user)
            transaction {
                usertable.insert {
                    it[email] = user.email
                    it[username] = user.username
                    it[passwordHash] = user.passwordHash
                    it[mailVerified] = user.mailVerified
                }
            }
            mailNotifier.sendMailRegister(user)
        }


    /**
     * Resets password of an account with the provided mail
     *
     * @param mail
     */
    fun resetPassword(mail: String) {
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
    fun changePassword(mail: String, password: String) {
            transaction {
                usertable.update({usertable.email eq mail}){
                    it[usertable.passwordHash] = password
                }
            }

        }

    /**
     * Set mail approved
     *
     * @param mail mail-address of the user which gets approved
     */
    public fun setMailApproved(mail: String) {
            transaction {
               usertable.update({usertable.email eq mail}) {
                   it[usertable.mailVerified] = true
               }
            }
        }

    /**
     * Checks based on the email-address and/or username if an user can be created with those values
     *
     * @param email email to check if there is a user with this mail
     * @param username username to check if there is a user with this username
     * @return
     */
    public fun checkRegisterPossible(email: String, username: String): Boolean {
            val userByMail = getUserByMail(email)
            val userByName = getUserByName(username)
            if (userByMail != null || userByName != null)
                return true
            return false
        }
    }
