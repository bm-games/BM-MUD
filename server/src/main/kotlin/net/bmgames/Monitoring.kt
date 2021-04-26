package net.bmgames

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*


fun Application.configureMonitoring() {
    install(CallLogging) {
        filter { call -> call.request.path().startsWith("/") }
    }

}
