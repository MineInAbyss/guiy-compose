package com.mineinabyss.guiy

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.components.Row
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.idofront.commands.execution.ExperimentalCommandDSL
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.slimjar.IdofrontSlimjar
import kotlinx.coroutines.delay
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

val guiy = Bukkit.getPluginManager().getPlugin("Guiy") as JavaPlugin

class GuiyPlugin : JavaPlugin() {
    override fun onEnable() {
        IdofrontSlimjar.loadToLibraryLoader(this)
        GuiyCommands()
    }
}

@OptIn(ExperimentalCommandDSL::class)
class GuiyCommands : IdofrontCommandExecutor() {
    override val commands = commands(guiy) {
        "guiy" {
            "menu" {
                playerAction {
                    val inv = guiy {
                        var item by remember { mutableStateOf(ItemStack(Material.WHITE_WOOL)) }
                        Row {
                            Item(item)
                            Item(ItemStack(Material.GRAVEL))
                            Item(ItemStack(Material.DIAMOND))
                        }

                        LaunchedEffect(Unit) {
                            while (true) {
                                item = ItemStack(Material.WHITE_WOOL)
                                delay(1000)
                                item = ItemStack(Material.RED_WOOL)
                                delay(1000)
                            }
                        }
                    }
                    player.openInventory(inv.inventory)
                }
            }
        }
    }

}
