<div align="center">

# Guiy

[![Maven](https://img.shields.io/maven-metadata/v?metadataUrl=https://repo.mineinabyss.com/releases/com/mineinabyss/guiy-compose/maven-metadata.xml)](https://repo.mineinabyss.com/#/releases/com/mineinabyss/guiy-compose)
[![Contribute](https://shields.io/badge/Contribute-e57be5?logo=github%20sponsors&style=flat&logoColor=white)](https://mineinabyss.com/contributing)
</div>

A Minecraft UI library for PaperMC, built on the Compose compiler.

While [Jetpack Compose](https://developer.android.com/jetpack/compose) is a commonly used UI toolkit in Android development,
*the Compose compiler is just a tool to dynamically [manage trees](https://arunkumar.dev/jetpack-compose-for-non-ui-tree-construction-and-code-generation/)*.
Guiy implements its own layouts, modifiers, and rendering specifically for Minecraft chest UI.
This means you can work with state exactly as you would with Jetpack Compose, but without any overhead from Android, and with concepts native to Minecraft.

> [!NOTE]
> Guiy is in active development as we continue to try new use-cases in our plugins. We can't promise api stability yet,
> but try to follow semver for breaking changes.

## Examples

See the `guiy-example` package for a full demonstration of project setup and different features.

### Entry

```kotlin
guiy(player) {
    ExampleMenu()
}
```

### Chest menu

```kotlin
@Composable
fun ExampleMenu() {
    // Guiy will dynamically update players, title, or height if you use a state.
    Chest(title = "Example", modifier = Modifier.height(4), onClose = { exit() /*back()*/ }) {
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
    // Set the width of an element (can use min/max constraints, or .size to set width and height)
    .width(3)
    // Fill based on parent constraints like Jetpack
    .fillmaxHeight()
    // Padding in # of blocks
    .padding(vertical = 1)
    // Place at an offset
    .offset(x = 1, y = 5)
    // Do actions on click
    .clickable { doSomething() }
```

### Alignment

Guiy provides Row, Column, and Box components based on Jetpack's, these come with Arrangement and Alignment too. We also
provide our components like Vertical/Horizontal Grids optimized for common Minecraft uses.

```kotlin
// A horizontal row of 10 items, with 1 space between each.
Row(horizontalArrangement = Arrangement.spacedBy(1)) {
    repeat(4) {
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

// Items aligned left to right, top to bottom, wrapped to be smaller than width, useful for pages of items!
VerticalGrid(Modifier.width(3)) {
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
our [main project](https://github.com/MineInAbyss/MineInAbyss/tree/master/mineinabyss-features/src/main/kotlin/com/mineinabyss/features/guilds/menus).

## Usage

### Gradle

#### build.gradle.kts

```kotlin
plugins {
    // Try to match version in guiy's build.gradle.kts
    id("org.jetbrains.kotlin.plugin.compose") version "<kotlin-version>"
    id("org.jetbrains.compose") version "<compose-version>"
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

Guiy does not package the Kotlin runtime, it depends on our helper plugin Idofront using Paper's isolated dependency
system.

- [Download](https://github.com/MineInAbyss/guiy-compose/releases/latest) and install Guiy into your plugin folder.
- [Download](https://github.com/MineInAbyss/Idofront/releases/latest) Idofront, a required dependency.
- Depend on Guiy in a [paper-plugin](https://docs.papermc.io/paper/dev/getting-started/paper-plugins), this will give you access to Guiy and any libraries in Idofront in an isolated manner.

## Thanks

- Google for creating Jetpack Compose.
- JetBrains for making it easy to use the compiler plugin outside of Android.
- A lot of the inital code to set up the Jetpack Compose environment comes from https://github.com/JakeWharton/mosaic.
