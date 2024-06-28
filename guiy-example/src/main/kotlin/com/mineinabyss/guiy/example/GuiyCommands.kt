package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.example.gui.*
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class GuiyCommands(plugin: GuiyExamplePlugin) : IdofrontCommandExecutor(), TabCompleter {
    override val commands = commands(plugin) {
        "guiyexample" {
            "arrangement" {
                playerAction {
                    guiy {
                        ArrangementMenu(player)
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
            listOf("arrangement", "animated", "creative", "cursor", "pagination")
        else listOf()
}
