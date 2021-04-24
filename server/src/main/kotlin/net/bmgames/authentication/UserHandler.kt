package net.bmgames.authentication

import net.bmgames.Communication.MailNotifier
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

public class UserHandler {


    /* protected fun getUser(token: JWT): User?{
        return null
    }*/

        val mailNotifier: MailNotifier = MailNotifier()
        var currentUser: User? = null

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


        fun resetPassword(mail: String) {
            val userByMail = getUserByMail(mail)
            if (userByMail != null) {
                val password = AuthHelper.hashPassword(AuthHelper.generatePassword())
                changePassword(mail, password)
                mailNotifier.sendMailReset(userByMail)
            }

        }

        fun changePassword(mail: String, password: String) {
            transaction {
                usertable.update({usertable.email eq mail}){
                    it[usertable.passwordHash] = password
                }
            }

        }

        public fun setMailApproved(mail: String) {
            transaction {
               usertable.update({usertable.email eq mail}) {
                   it[usertable.mailVerified] = true
               }
            }
        }

        public fun checkRegisterPossible(email: String, username: String): Boolean {
            val userByMail = getUserByMail(email)
            val userByName = getUserByName(username)
            if (userByMail != null || userByName != null)
                return true
            return false
        }
    }
