package com.mineinabyss.guiy.example

import com.mineinabyss.idofront.platforms.IdofrontPlatforms
import org.bukkit.plugin.java.JavaPlugin

class GuiyExamplePlugin : JavaPlugin() {
    override fun onLoad() {
        IdofrontPlatforms.load(this, "mineinabyss")
    }

    override fun onEnable() {
        GuiyCommands(this)
    }
}
