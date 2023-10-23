package com.mineinabyss.guiy.example

import org.bukkit.plugin.java.JavaPlugin

class GuiyExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        GuiyCommands(this)
    }
}
