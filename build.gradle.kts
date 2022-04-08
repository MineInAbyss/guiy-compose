import Com_mineinabyss_conventions_platform_gradle.Deps
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val idofrontVersion: String by project

plugins {
    kotlin("jvm")
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.nms")
    id("com.mineinabyss.conventions.publication")
    id("com.mineinabyss.conventions.testing")
    id("org.jetbrains.compose")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
        jvmTarget = "17"
    }
}


repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

dependencies {
    // MineInAbyss platform
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.kotlin.reflect)
    compileOnly(libs.minecraft.anvilgui)
    compileOnly(libs.minecraft.skedule)

    // Shaded
    api(compose.runtime) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    implementation("com.mineinabyss:idofront:$idofrontVersion")
    implementation(libs.idofront.nms)
}

val pluginPath = project.findProperty("plugin_path")!!

tasks {
    register<Copy>("copyJar") {
        from(findByName("reobfJar") ?: findByName("shadowJar"))
        into(pluginPath)
        doLast {
            println("Copied to plugin directory $pluginPath")
        }
    }

    assemble {
        dependsOn(reobfJar)
    }
    build {
        dependsOn("copyJar")
    }
}
