package net.bmgames

import arrow.core.right
import de.joshuagleitze.testfiles.kotest.testFiles
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmgames.ServerConfig.Companion.writeConfig
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.writeText

@OptIn(ExperimentalPathApi::class)
class ServerConfigTest : FunSpec({

    test("Should read config correctly") {
        val file = testFiles.createFile().apply { writeText(Json.encodeToString(DEMO_CONFIG)) }
        ServerConfig.readConfig(file.pathString) shouldBe DEMO_CONFIG.right()
    }

    test("Should write config correctly") {
        val file = testFiles.createFile().apply { DEMO_CONFIG.writeConfig(pathString, override = true) }
        Json.decodeFromString<ServerConfig>(file.readText()) shouldBe DEMO_CONFIG
    }
})
