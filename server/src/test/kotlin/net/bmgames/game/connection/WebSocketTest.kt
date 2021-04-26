package net.bmgames.game.connection

//import io.ktor.client.*
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import io.ktor.websocket.WebSockets
import kotlinx.coroutines.channels.ClosedReceiveChannelException
//import org.openjdk.jmh.annotations.Benchmark
//import org.openjdk.jmh.annotations.BenchmarkMode
//import org.openjdk.jmh.annotations.Mode
//
//
//val client = HttpClient {
//    install(io.ktor.client.features.websocket.WebSockets)
//}
//
//const val CLIENT_COUNT = 100000
//
//fun main(args: Array<String>) {
////     {
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
//        install(WebSockets)
//        routing {
//            webSocket("/echo") {
//                try {
//                    while (true) {
//                        val text = (incoming.receive() as Frame.Text).readText()
//                        outgoing.send(Frame.Text(text))
//                    }
//                } catch (e: ClosedReceiveChannelException) {
//                    // Do nothing!
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }.start()
////    }
//    org.openjdk.jmh.Main.main(args)
//}
//
//
//class WebSocketTest {
//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    suspend fun init() {
//        client.ws(
//            method = HttpMethod.Get,
//            host = "127.0.0.1",
//            port = 8080, path = "/route/path/to/ws"
//        ) { // this: DefaultClientWebSocketSession
//
//            // Send text frame.
//            send("Hello, Text frame")
//
//            // Send text frame.
//            send(Frame.Text("Hello World"))
//
//
//            // Receive frame.
//            val frame = incoming.receive()
//            when (frame) {
//                is Frame.Text -> println(frame.readText())
//                is Frame.Binary -> println(frame.readBytes())
//            }
//        }
//    }
//}

/*
class WebSocketTest : FunSpec({
    beforeSpec {

    }

    test("test") {
        val i = AtomicInteger(0)
        withTestApplication {
            application.install(WebSockets)
            application.routing {
                webSocket("/echo") {
                    i.incrementAndGet()
                    try {
                        while (true) {
                            val text = (incoming.receive() as Frame.Text).readText()
                            outgoing.send(Frame.Text(text))
                        }
                    } catch (e: ClosedReceiveChannelException) {
                        // Do nothing!
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }


            for (j in 1..CLIENT_COUNT) {
                async {
                    handleWebSocketConversation("/echo") { incoming, outgoing ->
                        val textMessages = listOf("HELLO", "WORLD", "bye")
                        for (msg in textMessages) {
                            outgoing.send(Frame.Text(msg))
                            assertEquals(msg, (incoming.receive() as Frame.Text).readText())
                        }
                    }
                }
            }
        }
        assertEquals(CLIENT_COUNT, i.get())
    }
})
*/

