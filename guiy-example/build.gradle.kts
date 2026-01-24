plugins {
    id(idofrontLibs.plugins.mia.kotlin.jvm.get().pluginId)
    id(idofrontLibs.plugins.mia.papermc.get().pluginId)
    id(idofrontLibs.plugins.mia.copyjar.get().pluginId)
    id(idofrontLibs.plugins.jetbrainsCompose.get().pluginId)
    id(idofrontLibs.plugins.compose.compiler.get().pluginId)
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
