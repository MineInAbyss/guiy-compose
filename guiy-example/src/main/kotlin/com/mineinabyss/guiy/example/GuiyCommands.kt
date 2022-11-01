package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.example.gui.MainMenu
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction

class GuiyCommands(val plugin: GuiyExamplePlugin) : IdofrontCommandExecutor() {
    override val commands = commands(plugin) {
        "guiyexample" {
            playerAction {
                guiy {
                    MainMenu(player)
                }
            }
        }
    }
}
