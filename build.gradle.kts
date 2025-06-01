plugins {
    kotlin("jvm") version "2.1.21"
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

tasks.register("createDay") {
    val day: String by project
    val title: String by project
    val formattedDay = day.padStart(2, '0')

    fun replaceTemplates(line: String): String = line.replace("\\\$DAY".toRegex(), formattedDay).replace("\\\$NAME".toRegex(), title)

    val dayContent = layout.projectDirectory.file("src/main/resources/templates/day_template.kt").asFile.readLines().map { replaceTemplates(it) }
    val testContent = layout.projectDirectory.file("src/main/resources/templates/test_template.kt").asFile.readLines().map { replaceTemplates(it) }

    layout.projectDirectory.file("src/main/kotlin/Day${formattedDay}.kt").asFile.writeText(dayContent.joinToString("\n"))
    layout.projectDirectory.file("src/test/kotlin/Day${formattedDay}Test.kt").asFile.writeText(testContent.joinToString("\n"))
    layout.projectDirectory.file("src/main/resources/Day${formattedDay}.txt").asFile.createNewFile()
    layout.projectDirectory.file("src/test/resources/Day${formattedDay}.txt").asFile.createNewFile()
}
