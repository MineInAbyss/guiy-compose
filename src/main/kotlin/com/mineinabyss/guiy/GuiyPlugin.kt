package com.mineinabyss.guiy

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.canvases.Chest
import com.mineinabyss.guiy.inventory.GuiyEventListener
import com.mineinabyss.guiy.inventory.GuiyScopeManager
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.guiy.layout.Row
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.clickable
import com.mineinabyss.guiy.nodes.InventoryCanvas
import com.mineinabyss.idofront.commands.execution.ExperimentalCommandDSL
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.plugin.registerEvents
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryCloseEvent
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
        Bukkit.getOnlinePlayers().filter { it.openInventory.topInventory.holder is InventoryCanvas }
            .forEach { it.closeInventory(InventoryCloseEvent.Reason.PLUGIN) }
    }
}

sealed class Screen {
    object Default : Screen()
    data class Details(val text: String) : Screen()
}

@OptIn(ExperimentalCommandDSL::class)
class GuiyCommands : IdofrontCommandExecutor() {
    override val commands = commands(guiyPlugin) {
        "guiy" {
            "menu" {
                playerAction {
                    guiy {
                        var screenState by remember { mutableStateOf<Screen>(Screen.Default) }
                        var height by remember { mutableStateOf(1) }

                        when (val screen = screenState) {
                            is Screen.Default -> Chest(
                                viewers,
                                title = "Test",
                                height = height,
                                onClose = { exit() }) {
                                val white = remember { ItemStack(Material.WHITE_WOOL) }
                                val red = remember { ItemStack(Material.RED_WOOL) }

                                var isRed by remember { mutableStateOf(false) }
                                Row(Modifier.at(1, 2)) {
                                    for (i in 1..9) {
                                        Item(ItemStack(Material.STONE).editItemMeta {
                                            setDisplayName("$i")
                                        }, Modifier.clickable { screenState = Screen.Details("$i")  })
                                    }
                                }

//                                Item(ItemStack(Material.CACTUS), Modifier.at(3, 1))
//                                Item(ItemStack(Material.CACTUS), Modifier.at(3, 2))
                            }
                            is Screen.Details -> Chest(viewers, title = screen.text, height = height, onClose = {
                                screenState = Screen.Default
                            }) {
                            }
                        }

                        LaunchedEffect(Unit) {
                            viewers.add(player)
                            for (i in 1..6) {
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
