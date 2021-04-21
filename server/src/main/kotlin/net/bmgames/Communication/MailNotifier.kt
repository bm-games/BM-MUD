package net.bmgames.Communication

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailNotifier : Notifier {
    override fun send(user: User?,message: String?, mailSubject: String?, messageType: String) {
        val emailFrom = "support@bm-games.net"
        val emailTo = user.email
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
            addRecipient(Message.RecipientType.TO, InternetAddress(emailTo))
            subject = mailSubject
            setContent(message, "text/html; charset=utf-8")
        }
        Transport.send(mimeMessage)
    }
}