import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kotest_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.32"
    id("org.jetbrains.dokka") version "1.4.30"
}

group = "net.bmgames"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-webjars:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.webjars:jquery:3.2.1")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation ("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation ("io.kotest:kotest-assertions-core-jvm:$kotest_version")
    testImplementation ("io.kotest:kotest-property-jvm:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-ktor:$kotest_version")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        jvmTarget = "1.8"
    }
}

