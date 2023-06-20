import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val idofrontVersion: String by project

plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.nms")
    id("com.mineinabyss.conventions.autoversion")
    id("com.mineinabyss.conventions.publication")
    id("com.mineinabyss.conventions.testing")
    id("com.mineinabyss.conventions.copyjar")
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
