import Com_mineinabyss_conventions_platform_gradle.Deps
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val idofrontVersion: String by project

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.copyjar")
    id("com.mineinabyss.conventions.publication")
    id("com.mineinabyss.conventions.testing")
    id("org.jetbrains.compose") version "1.1.1"
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
    mavenCentral()
    google()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://dl.bintray.com/korlibs/korlibs")
}

dependencies {
    // MineInAbyss platform
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(Deps.kotlinx.coroutines)
    compileOnly(Deps.kotlin.reflect)
    compileOnly(Deps.minecraft.anvilgui)
    // TODO update to jitpack in idofront
    compileOnly("com.github.okkero:Skedule:v1.2.6")

    // Shaded
    api(compose.runtime) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }
    api(compose.foundation)

//    api(compose.foundation)
    implementation("com.mineinabyss:idofront:$idofrontVersion")
}
