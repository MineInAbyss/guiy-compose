package com.mineinabyss.guiy

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Row
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.inventory.GuiyEventListener
import com.mineinabyss.guiy.inventory.GuiyScopeManager
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.guiy.remember.rememberViewers
import com.mineinabyss.idofront.commands.execution.ExperimentalCommandDSL
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.plugin.registerEvents
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

val guiyPlugin = Bukkit.getPluginManager().getPlugin("Guiy") as JavaPlugin

class GuiyPlugin : JavaPlugin() {
    override fun onEnable() {
//        IdofrontSlimjar.loadToLibraryLoader(this)
        GuiyCommands()
        registerEvents(GuiyEventListener())
    }

    override fun onDisable() {
        GuiyScopeManager.scopes.forEach { it.cancel() }
    }
}

@OptIn(ExperimentalCommandDSL::class)
class GuiyCommands : IdofrontCommandExecutor() {
    override val commands = commands(guiyPlugin) {
        "guiy" {
            "menu" {
                playerAction {
                    guiy {
                        val viewers by rememberViewers()
                        var height by remember { mutableStateOf(1) }
                        Chest(viewers, title = "Test", height = height, onClose = { exit() }) {
                            val coroutine = rememberCoroutineScope()
                            val white = remember { ItemStack(Material.WHITE_WOOL) }
                            val red = remember { ItemStack(Material.RED_WOOL) }

                            var isRed by remember { mutableStateOf(false) }
                            Row {
                                Item(if (isRed) red else white, onClick = {
                                    coroutine.launch {
                                        delay(1000)
                                        isRed = !isRed
                                    }
                                })
                            }
                        }

                        LaunchedEffect(Unit) {
                            viewers.add(player)
                            for(i in 1..6) {
                                height = i
                                println(height)
                                delay(100)
                            }
                        }
                    }
                }
            }
        }
    }
}
