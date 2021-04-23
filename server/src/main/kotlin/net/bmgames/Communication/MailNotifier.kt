package net.bmgames.Communication

import net.bmgames.authentication.AuthHelper
import net.bmgames.authentication.User
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailNotifier : Notifier {
    override fun send(recipient: String, message: String, mailSubject: String) {
        val emailFrom = "support@bm-games.net"
        val properties = System.getProperties()
        with(properties)
        {//load Config later on
            put("mail.smtp.host", "mail.bm-games.net")
            put("mail.smtp.port", "25")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.auth", "true")
        }
        val auth = object: Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication =
                PasswordAuthentication(emailFrom, "G6!XT7YSzfMQRAsk")
        }
        val session = Session.getDefaultInstance(properties, auth)
        val mimeMessage = MimeMessage(session)
        with(mimeMessage){
            setFrom(InternetAddress(emailFrom))
            addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
            subject = mailSubject
            setContent(message, "text/html; charset=utf-8")
        }
        Transport.send(mimeMessage)
    }

    fun sendMailReset(user: User) {
        val mailSubject = "Reset your password | BM-Games |  "
        val recipient: String = user.email
        val message: String =   "<html><body><h1>Reset your password.</h1>" +
                                "<p>Click the link below to reset your password</p><p>${AuthHelper.unhashPassword(user.passwordHash)}</p></body></html>"

        send(recipient, message, mailSubject)
    }
    fun sendMailRegister(user: User) {
        val mailSubject = "Registration confirmation | BM-Games | SWE-Project  "
        val recipient: String = user.email
        val message: String =   "<html><body><h1>Confirm your registration.</h1>" +
                                "<p>Click the link below to confirm your registration</p><a>${user.registrationKey}</a></body></html>"

        send(recipient, message, mailSubject)
    }
}