import Com_mineinabyss_conventions_platform_gradle.Deps
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val idofrontVersion: String by project

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.copyjar")
    id("org.jetbrains.compose")
}

dependencies {
    // MineInAbyss platform
    compileOnly(project(":"))
    compileOnly(libs.kotlinx.coroutines)
    implementation("com.mineinabyss:idofront:$idofrontVersion")
}

tasks {
    build {
        dependsOn(project(":").tasks.build)
    }
}
