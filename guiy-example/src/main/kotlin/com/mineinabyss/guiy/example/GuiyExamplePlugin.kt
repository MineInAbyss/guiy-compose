package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.example.gui.*
import com.mineinabyss.guiy.inventory.guiy
import com.mineinabyss.idofront.commands.brigadier.commands
import org.bukkit.plugin.java.JavaPlugin

class GuiyExamplePlugin : JavaPlugin() {
    override fun onEnable() = commands {
        "guiyexample" {
            "arrangement" {
                playerExecutes {
                    guiy { ArrangementMenu() }
                }
            }
            "animated" {
                playerExecutes {
                    guiy { AnimatedTitle(player) }
                }
            }
            "creative" {
                playerExecutes {
                    guiy { CreativeMenu() }
                }
            }
            "cursor" {
                playerExecutes {
                    guiy { Cursor() }
                }
            }
            "pagination" {
                playerExecutes {
                    guiy { PaginatedMenu() }
                }
            }
            "scrolling" {
                playerExecutes {
                    guiy { ScrollingMenu() }
                }
            }
        }
    }
}
