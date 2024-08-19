package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.example.gui.AnimatedTitle
import com.mineinabyss.guiy.example.gui.ArrangementMenu
import com.mineinabyss.guiy.example.gui.CreativeMenu
import com.mineinabyss.guiy.example.gui.Cursor
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.idofront.commands.brigadier.commands
import org.bukkit.plugin.java.JavaPlugin

class GuiyExamplePlugin : JavaPlugin() {
    override fun onEnable() = commands {
        "guiyexample" {
            "arrangement" {
                playerExecutes {
                    guiy {
                        ArrangementMenu(player)
                    }
                }
            }
            "animated" {
                playerExecutes {
                    guiy {
                        AnimatedTitle(player)
                    }
                }
            }
            "creative" {
                playerExecutes {
                    guiy {
                        CreativeMenu(player)
                    }
                }
            }
            "cursor" {
                playerExecutes {
                    guiy {
                        Cursor(player)
                    }
                }
            }
            "pagination" {
                playerExecutes {
                    guiy {
                        com.mineinabyss.guiy.example.gui.PaginatedMenu(player)
                    }
                }
            }
        }
    }
}
