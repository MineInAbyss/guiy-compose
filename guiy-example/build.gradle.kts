val idofrontVersion: String by project

plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.copyjar")
    id("org.jetbrains.compose")
}

dependencies {
    // MineInAbyss platform
    compileOnly(project(":"))
    compileOnly(libs.kotlinx.coroutines)
    implementation("com.mineinabyss:idofront-commands:$idofrontVersion")
    implementation("com.mineinabyss:idofront-platform-loader:$idofrontVersion")
}

tasks {
    build {
        dependsOn(project(":").tasks.build)
    }
}
