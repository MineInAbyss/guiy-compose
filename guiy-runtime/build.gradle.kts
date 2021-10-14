import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.testing")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api(project("compose:runtime"))
    // Download at runtime
    implementation(kotlin("stdlib-jdk8"))
}
