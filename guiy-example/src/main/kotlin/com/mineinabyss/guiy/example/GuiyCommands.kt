package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.example.gui.AnimatedTitle
import com.mineinabyss.guiy.example.gui.CreativeMenu
import com.mineinabyss.guiy.example.gui.Cursor
import com.mineinabyss.guiy.example.gui.PaginatedMenu
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class GuiyCommands(val plugin: GuiyExamplePlugin) : IdofrontCommandExecutor(), TabCompleter {
    override val commands = commands(plugin) {
        "guiyexample" {
            "animated" {
                playerAction {
                    guiy {
                        AnimatedTitle(player)
                    }
                }
            }
            "creative" {
                playerAction {
                    guiy {
                        CreativeMenu(player)
                    }
                }
            }
            "cursor" {
                playerAction {
                    guiy {
                        Cursor(player)
                    }
                }
            }
            "pagination" {
                playerAction {
                    guiy {
                        PaginatedMenu(player)
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
            listOf("animated", "creative", "cursor", "pagination")
        else listOf()
}
