package net.bmgames.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*


fun Application.configureMonitoring() {
    install(CallLogging) {
        filter { call -> call.request.path().startsWith("/") }
    }

}
