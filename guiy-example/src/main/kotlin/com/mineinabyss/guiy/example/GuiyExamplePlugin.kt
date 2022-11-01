package com.mineinabyss.guiy.example

import com.mineinabyss.idofront.platforms.Platforms
import org.bukkit.plugin.java.JavaPlugin

class GuiyExamplePlugin : JavaPlugin() {
    override fun onLoad() {
        Platforms.load(this, "mineinabyss")
    }

    override fun onEnable() {
        GuiyCommands(this)
    }
}
