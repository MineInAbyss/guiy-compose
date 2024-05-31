package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.example.gui.AnimatedTitle
import com.mineinabyss.guiy.example.gui.MainMenu
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class GuiyCommands(val plugin: GuiyExamplePlugin) : IdofrontCommandExecutor(), TabCompleter {
    override val commands = commands(plugin) {
        "guiyexample" {
            "creative" {
                playerAction {
                    guiy {
                        MainMenu(player)
                    }
                }
            }
            "animated" {
                playerAction {
                    guiy {
                        AnimatedTitle(player)
                    }
                }
            }
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): List<String> =
        if (command.name == "guiyexample")
            listOf("creative", "animated")
        else listOf()
}
