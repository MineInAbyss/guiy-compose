<div align="center">

# Guiy

[![Java CI with Gradle](https://github.com/MineInAbyss/guiy-compose/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/MineInAbyss/guiy-compose/actions/workflows/gradle-ci.yml)
[![Maven](https://img.shields.io/maven-metadata/v?metadataUrl=https://repo.mineinabyss.com/releases/com/mineinabyss/guiy-compose/maven-metadata.xml)](https://repo.mineinabyss.com/#/releases/com/mineinabyss/guiy-compose)
[![Contribute](https://shields.io/badge/Contribute-e57be5?logo=github%20sponsors&style=flat&logoColor=white)](https://wiki.mineinabyss.com/contribute/)
</div>

A Spigot/PaperMC UI library built on the [Jetpack Compose](https://developer.android.com/jetpack/compose) compiler.

If you are new to Compose, please read the link above. In short, it is a declarative UI library that makes working with
state nice, gives easy access to coroutines, and helps write complex UI faster.

## Beta status

We can't promise api stability yet, for the most part none of the existing elements should ever break entirely, but we
may change some behaviour like how `Grid` organizes itself.

## Examples

### Entry

```kotlin
guiy {
    ExampleMenu(player)
}
```

### Chest menu

```kotlin
@Composable
fun GuiyOwner.ExampleMenu(player: Player) {
    // Guiy will dynamically update players, title, or height if you use a state.
    Chest(setOf(player), title = "Example", height = 4, onClose = { exit() /*reopen()*/ }) {
        ToggleButton()
    }
}
```

### Toggle button

```kotlin
val RED = ItemStack(Material.RED_WOOL)
val GREEN = ItemStack(Material.GREEN_WOOL)

@Composable
fun ToggleButton() {
    var enabled by remember { mutableStateOf(false) }
    val display = if (enabled) GREEN else RED
    Item(display, Modifier.clickable {
        enabled = !enabled
    })
}
```

### Modifiers

We use a similar modifier system to Jetpack Compose.

```kotlin
// Entry to modifiers, though you are encouraged to pass a modifier parameter into your composables.
Modifier
    // Set the size of an element (can use min/max constraints too)
    .size(width = 2, height = 2)
    // Place at an absolute offset
    .at(x = 1, y = 5)
    // Do actions on click
    .clickable { doSomething() }
```

### Alignment

```kotlin
// A horizontal group of 10 items
Row {
    repeat(10) {
        Item(...)
    }
}
// Same but vertical
Column { ... }

// Two rows on top of each other
Column {
    Row { ... }
    Row { ... }
}

// Items aligned left to right, top to bottom, wrapped to be smaller than width.
Grid(Modifier.width(3)) {
    repeat(7) {
        Item(...)
    }
}
/* Result:
    III
    III
    I--   */
```

### Coroutines

LaunchedEffect support, same as in Jetpack Compose:

```kotlin
fun TimedToggle() {
    var enabled by remember { mutableStateOf(false) }
    val display = if (enabled) GREEN else RED
    Item(display)

    LaunchedEffect(Unit) {
        // Button will flash 10 times, with 1 second intervals
        repeat(10) {
            toggled = !toggled
            delay(1000)
        }
    }
}
```

### Real world use

We are using this project internally, so you should be able to find up-to-date usage in
our [main project](https://github.com/MineInAbyss/MineInAbyss/tree/master/mineinabyss-features/src/main/kotlin/com/mineinabyss/guilds/menus).

## Usage

### Gradle

#### build.gradle.kts

```kotlin
plugins {
    // Match version in guiy's build.gradle.kts
    id("org.jetbrains.compose") version "1.x.x"
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://repo.mineinabyss.com/releases")
}

dependencies {
    compileOnly("com.mineinabyss:guiy-compose:x.y.z")
}
```

#### settings.gradle.kts

```kotlin
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
```

### Server setup

Guiy does not package the Kotlin runtime in itself, it uses our library idofront to load shared dependencies in an
isolated way.

- [Download](https://github.com/MineInAbyss/guiy-compose/releases/latest) and install Guiy into your plugin folder.
- [Download](https://github.com/MineInAbyss/Idofront/releases/latest) and install our dependency platform into your
  plugin folder.
- Use our [platform loader](https://github.com/MineInAbyss/Idofront/tree/master/idofront-platform-loader) to load Kotlin
  for yourself.

There is currently no support for shading guiy.

## Thanks

- Google for creating Jetpack Compose.
- JetBrains for making it easy to use the compiler plugin outside of Android.
- A lot of the inital code to set up the Jetpack Compose environment comes from https://github.com/JakeWharton/mosaic.
