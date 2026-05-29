import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    alias(miaLibs.plugins.mia.kotlin.jvm)
    alias(miaLibs.plugins.mia.papermc)
    alias(miaLibs.plugins.mia.nms)
    alias(miaLibs.plugins.mia.copyjar)
    alias(miaLibs.plugins.mia.publication)
    alias(miaLibs.plugins.mia.autoversion)
    alias(miaLibs.plugins.mia.docs)
    alias(miaLibs.plugins.jetbrainsCompose)
    alias(miaLibs.plugins.compose.compiler)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://repo.mineinabyss.com/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo.codemc.org/repository/maven-public/")
    }
}

dependencies {
    // MineInAbyss platform
    compileOnly(miaLibs.kotlinx.coroutines)
    compileOnly(miaLibs.minecraft.mccoroutine)
    compileOnly(miaLibs.kotlin.reflect)

    // Shaded
    api(compose.runtime) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    implementation(miaLibs.bundles.idofront.core)
    api(libs.compose.mini.layout)
    api(libs.compose.mini.runtime)
    api(libs.compose.mini.modifier)
    implementation(miaLibs.idofront.nms)
}

paper {
    main = "com.mineinabyss.guiy.GuiyPlugin"
    name = "Guiy"
    description = "Minecraft UI built on Jetpack Compose"
    author = "Offz"
    serverDependencies {
        register("Idofront") {
            joinClasspath = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}
