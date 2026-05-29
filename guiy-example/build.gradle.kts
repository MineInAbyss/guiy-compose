import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id(miaLibs.plugins.mia.kotlin.jvm.get().pluginId)
    id(miaLibs.plugins.mia.papermc.get().pluginId)
    id(miaLibs.plugins.mia.copyjar.get().pluginId)
    id(miaLibs.plugins.jetbrainsCompose.get().pluginId)
    id(miaLibs.plugins.compose.compiler.get().pluginId)
}

dependencies {
    // MineInAbyss platform
    compileOnly(project(":"))
    compileOnly(miaLibs.kotlinx.coroutines)
    compileOnly(miaLibs.idofront.commands)
}

tasks {
    build {
        dependsOn(project(":").tasks.build)
    }
}

paper {
    name = "GuiyExample"
    main = "com.mineinabyss.guiy.example.GuiyExamplePlugin"
    author = "Offz"
    serverDependencies {
        register("Guiy") {
            joinClasspath = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}
