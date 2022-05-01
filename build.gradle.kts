import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val idofrontVersion: String by project

plugins {
    kotlin("jvm")
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.nms")
    id("com.mineinabyss.conventions.publication")
    id("com.mineinabyss.conventions.testing")
    id("com.mineinabyss.conventions.copyjar")
    id("org.jetbrains.compose")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
        jvmTarget = "17"
    }
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    // MineInAbyss platform
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.minecraft.mccoroutine)
    compileOnly(libs.kotlin.reflect)
    compileOnly(libs.minecraft.anvilgui)

    // Shaded
    api(compose.runtime) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    implementation(libs.idofront.core)
    implementation(libs.idofront.nms)
}
