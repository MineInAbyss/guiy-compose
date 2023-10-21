import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val idofrontVersion: String by project

plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    alias(libs.plugins.mia.papermc)
    alias(libs.plugins.mia.nms)
    alias(libs.plugins.mia.copyjar)
    alias(libs.plugins.mia.publication)
    alias(libs.plugins.mia.autoversion)
    alias(libs.plugins.mia.testing)
    alias(libs.plugins.compose)
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
        jvmTarget = "17"
    }
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    // MineInAbyss platform
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.minecraft.mccoroutine)
    compileOnly(libs.kotlin.reflect)
    compileOnly(libs.minecraft.anvilgui)

    // Shaded
    api(compose.runtime) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    implementation(libs.bundles.idofront.core)
    implementation(libs.idofront.nms)
}
