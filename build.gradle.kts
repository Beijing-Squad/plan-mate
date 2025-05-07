plugins {
    kotlin("jvm") version "2.1.10"
    id("jacoco")
    id("com.github.kt3k.coveralls") version "2.12.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    implementation("io.insert-koin:koin-core:4.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.12.0")
    testImplementation("io.mockk:mockk:1.14.0")
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }

    dependsOn(tasks.test)
}

coveralls {
    jacocoReportPath = "${layout.buildDirectory}/reports/jacoco/test/jacocoTestReport.xml"
}

kotlin {
    jvmToolchain(22)
}

application {
    applicationDefaultJvmArgs = listOf(
        "-DMONGO_CONNECTION_STRING=${project.findProperty("MONGO_CONNECTION_STRING")}"
    )
}
