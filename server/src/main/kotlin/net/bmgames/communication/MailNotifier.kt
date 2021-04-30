package net.bmgames.communication

import net.bmgames.ServerConfig
import net.bmgames.authentication.AuthHelper
import net.bmgames.authentication.User
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * Mail notifier
 *
 * @constructor Create empty Mail notifier
 */
class MailNotifier(val config: ServerConfig) : Notifier {
    /**
     * Sends a mail to the user using the supplied parameters via smtp.
     *
     * @param recipient Recipient user
     * @param message Message in HTML format which will be sent to the recipient.
     * @param subject Self-explanatory
     */
    override fun send(recipient: User, subject: String, message: String) {
        val emailFrom = config.emailAddress
        val properties = System.getProperties()
        with(properties) {
            put("mail.smtp.host", config.smtpHost)
            put("mail.smtp.port", config.smtpPort)
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.auth", "true")
        }
        val auth = object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication =
                PasswordAuthentication(emailFrom, config.emailPassword)
        }
        val session = Session.getDefaultInstance(properties, auth)
        val mimeMessage = MimeMessage(session)
        with(mimeMessage) {
            setFrom(InternetAddress(emailFrom))
            addRecipient(Message.RecipientType.TO, InternetAddress(recipient.email))
            setSubject(subject)
            setContent(message, "text/html; charset=utf-8")
        }
        Transport.send(mimeMessage)
    }

    /**
     * Sends(Generates) the user a mail with a generated password so that the user can use it to assign a new password.
     *
     * @param user
     */
    fun sendMailReset(user: User) {
        val mailSubject = "Reset your password | BM-Games "
        val message: String =
            """<html lang="en"><body>            
                <h1>Reset your password.</h1>
                <p>Click the link below to reset your password</p>
                <p>${AuthHelper.unhashPassword(user.passwordHash)}</p>
                </body></html>"""

        send(user, mailSubject, message)
    }

    /**
     * Sends(Generates) a registration email to the user so that he can complete his registration using a generated code.
     *
     * @param user
     */
    fun sendMailRegister(user: User, registrationKey: String) {
        val mailSubject = "Registration confirmation | BM-Games | SWE-Project  "
        val message: String =
            """<html><body>
                <h1>Confirm your registration.</h1>
                <p>Click the link below to confirm your registration</p>
                <a>${registrationKey}</a>
                </body></html>"""

        send(user, mailSubject, message)
    }
}
