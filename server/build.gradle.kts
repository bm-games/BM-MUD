import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kotest_version: String by project
val jacoco_version: String by project
val exposedVersion: String by project
val arrow_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.32"
    kotlin("kapt") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"

    // Analysis & Reports
    jacoco
    id("org.jetbrains.dokka") version "1.4.32"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"

    // FatJar
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "net.bmgames"
version = "1.0.1"
application {
    mainClassName = "net.bmgames.MainKt"
    mainClass.set("net.bmgames.MainKt")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
//    Ktor
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-webjars:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")


//    Testing
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotest_version")
    testImplementation("io.kotest:kotest-property-jvm:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-ktor:$kotest_version")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.0.2")
    testImplementation("de.joshuagleitze:kotest-files:2.0.0")
    testImplementation("io.mockk:mockk:1.11.0")
//    implementation("org.openjdk.jmh:jmh-core:1.29")
//    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.29")

//    DB
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.2.5")
    implementation("com.h2database:h2:1.4.199")
//    implementation("postgresql:postgresql:jar:9.1-901.jdbc4")

//    Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

//    Utils
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("io.arrow-kt:arrow-fx-coroutines:$arrow_version")
    implementation("io.arrow-kt:arrow-optics:$arrow_version")
    kapt("io.arrow-kt:arrow-meta:$arrow_version")

//    CLI Parser
    implementation("com.github.ajalt.clikt:clikt:3.1.0")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    /*jacoco{
        filter {
            excludeTestsMatching("net.bmgames.state.model.*Kt") //Filter out generated sources
        }}*/
}

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
    }
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude("net/bmgames/state/model/**")
            }
        }))
    }

}

tasks.withType<Detekt> {
    jvmTarget = "1.8"
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        configureEach {
            includeNonPublic.set(true)
            suppressInheritedMembers.set(true)
        }
    }
}

detekt {
    config = files("$rootDir/config/detekt/detekt.yml")
    ignoreFailures = true
    parallel = true

    reports {
        html.enabled = true
        xml.enabled = true
        txt.enabled = false
    }
}

tasks.withType<ShadowJar> {

    manifest {
        attributes["Implementation-Title"] = "Black Mamba Games MUD"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = application.mainClass.get()
    }

}
