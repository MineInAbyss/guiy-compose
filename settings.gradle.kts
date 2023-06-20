pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://repo.mineinabyss.com/releases")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    val idofrontVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.mineinabyss.conventions"))
                useVersion(idofrontVersion)
        }
    }
}

dependencyResolutionManagement {
    val idofrontVersion: String by settings

    repositories {
        maven("https://repo.mineinabyss.com/releases")
    }

    versionCatalogs.create("libs").from("com.mineinabyss:catalog:$idofrontVersion")
}

rootProject.name = "guiy-compose"

include(
    "guiy-example"
)
