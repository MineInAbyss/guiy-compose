val idofrontVersion: String by project

plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    alias(libs.plugins.mia.papermc)
    alias(libs.plugins.mia.copyjar)
    alias(libs.plugins.compose)
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
