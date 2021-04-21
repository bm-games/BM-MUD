package net.bmgames.Communication

interface Notifier {
    fun send(message: String?, mailSubject: String?)
}