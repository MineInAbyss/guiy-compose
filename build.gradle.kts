import Com_mineinabyss_conventions_platform_gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val idofrontVersion: String by project

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.slimjar")
    id("com.mineinabyss.conventions.copyjar")
    id("com.mineinabyss.conventions.publication")
    id("com.mineinabyss.conventions.testing")
    id("org.jetbrains.compose") version "1.0.0-alpha4-build398"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
        jvmTarget = "16"
    }
}


repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://dl.bintray.com/korlibs/korlibs")
    maven("https://jitpack.io")
}

dependencies {
    // Download at runtime
    slim(kotlin("stdlib-jdk8"))
    implementation(Deps.minecraft.skedule)

    // Shaded
    implementation(compose.runtime)
    implementation("com.mineinabyss:idofront:$idofrontVersion")
}
