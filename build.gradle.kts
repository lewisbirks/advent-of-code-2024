plugins {
    kotlin("jvm") version "2.1.0"
}


dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}


tasks.test {
    useJUnitPlatform()
}

