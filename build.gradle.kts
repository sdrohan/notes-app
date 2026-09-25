plugins {
    kotlin("jvm") version "2.3.21"
    // Plugin for Dokka - KDoc generating tool
    id("org.jetbrains.dokka") version "2.2.0"
    jacoco
    application
}

group = "ie.setu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Source: https    ://mvnrepository.com/artifact/io.github.oshai/kotlin-logging-jvm
    implementation("io.github.oshai:kotlin-logging-jvm:8.0.4")
    // Source: https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.18")
    // For Streaming XML and JSON objects to files
    implementation("tools.jackson.core:jackson-databind:3.1.4")
    implementation("tools.jackson.module:jackson-module-kotlin:3.1.4")
    implementation("tools.jackson.dataformat:jackson-dataformat-xml:3.1.4")
    // For generating a Dokka Site from KDoc
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.2.0")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
    //report is always generated after tests run
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}












