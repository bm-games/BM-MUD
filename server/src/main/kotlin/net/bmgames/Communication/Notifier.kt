package net.bmgames.Communication

/**
 * Notifier
 *
 * @constructor Create empty Notifier
 */
interface Notifier {
    /**
     * Interface to send Message
     *
     * @param recipient
     * @param message
     * @param mailSubject
     */
    fun send(
        recipient: String,
        message: String,
        mailSubject: String)
}