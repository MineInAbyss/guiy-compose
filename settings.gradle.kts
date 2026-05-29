pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://repo.mineinabyss.com/releases")
        maven("https://repo.mineinabyss.com/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencyResolutionManagement {
    val miaLibs: String by settings

    repositories {
        maven("https://repo.mineinabyss.com/releases")
        maven("https://repo.mineinabyss.com/snapshots")
    }

    versionCatalogs.create("miaLibs").from("com.mineinabyss:catalog:$miaLibs")
}

rootProject.name = "guiy-compose"

include(
    "guiy-example",
)

//includeBuild("../compose-mini")