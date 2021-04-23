package net.bmgames.Communication

interface Notifier {
    fun send(recipient: String,message: String, mailSubject: String)
}