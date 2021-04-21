package net.bmgames.Communication

interface Notifier {
    fun send(user: User?,message: String?, mailSubject: String?, messageType: String)
}