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
 * @constructor Create empty mail notifier
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
    fun sendMailReset(user: User, password: String) {
        val mailSubject = "Passwort zurücksetzen | BM-Games "
        val message: String =
            """<html lang="de">
                <body style="background-color:#c0c0c0;">
                <h1><center>Dein Passwort wurde zurückgesetzt!</h1>
                <p><center>Dear ${user.username}</p>
                <p><center>Dein passwort wurde erfolgreich zurückgesetzt!</p>
                <p><center>Dein neues passwort ist:</p>
                <span style="border: 3px solid black;">${password}</span>
                <p><center> Melde dich mit deinem neuen Passwort an und lege ein eigenes Passwort an!</p>
                </body>
                </html>"""
                //HTML by Jakub
        send(user, mailSubject, message)
    }

    /**
     * Sends(Generates) a registration email to the user so that he can complete his registration using a generated code.
     *
     * @param user
     */
    fun sendMailRegister(user: User, registrationKey: String) {
        val mailSubject = "Registrierung verfolständigen| BM-Games | SWE-Project  "
        val message: String =
            """<html><body style="background-color:#c0c0c0;">
            <h1><center>Bestätige deine Registrierung</h1>
            <p><center>Dear ${user.username}</p>
            <p><center>Bitte bestätige deine registrierung, indem du auf den folgenden Link drückst:</p>
            <a href="http://play.bm-games.net/api/auth/verify/${registrationKey}" target="_blank">Click here!</a>
            <p><center>Oder kopiere diesen Link:</p>
            <span style="border: 3px solid black;">http://play.bm-games.net/api/auth/verify/${registrationKey}</span>
            </body></html>"""
            //HTML by Jakub
        send(user, mailSubject, message)
    }
}
