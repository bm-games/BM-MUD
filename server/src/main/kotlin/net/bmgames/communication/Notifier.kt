package net.bmgames.communication

import net.bmgames.authentication.User

/**
 * Notifier provides a service to send notifications to a User. Currently this is implemented via mail.
 */
interface Notifier {

    fun send(
        recipient: User,
        subject: String,
        message: String,
    )
}
