val idofrontVersion: String by project

plugins {
    alias(idofrontLibs.plugins.mia.kotlin.jvm)
    alias(idofrontLibs.plugins.mia.papermc)
    alias(idofrontLibs.plugins.mia.copyjar)
    alias(idofrontLibs.plugins.compose)
}

dependencies {
    // MineInAbyss platform
    compileOnly(project(":"))
    compileOnly(idofrontLibs.kotlinx.coroutines)
    compileOnly(idofrontLibs.idofront.commands)
}

tasks {
    build {
        dependsOn(project(":").tasks.build)
    }
}
