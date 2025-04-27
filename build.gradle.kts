plugins {
    kotlin("jvm") version "2.1.10"
    id("jacoco")
    id("com.github.kt3k.coveralls") version "2.12.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.12.0")
    testImplementation("io.mockk:mockk:1.14.0")
}

jacoco {
    toolVersion = "0.8.10"
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