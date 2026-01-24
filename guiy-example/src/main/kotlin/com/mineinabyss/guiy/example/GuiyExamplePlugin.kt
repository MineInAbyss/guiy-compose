package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.canvas.guiy
import com.mineinabyss.guiy.example.gui.*
import com.mineinabyss.idofront.commands.brigadier.commands
import org.bukkit.plugin.java.JavaPlugin

class GuiyExamplePlugin : JavaPlugin() {
    override fun onEnable() = commands {
        "guiyexample" {
            "arrangement" {
                playerExecutes {
                    guiy(player) { ArrangementMenu() }
                }
            }
            "animated" {
                playerExecutes {
                    guiy(player) { AnimatedTitle() }
                }
            }
            "autoReopen" {
                playerExecutes {
                    guiy(player) { AutoReopen() }
                }
            }
            "creative" {
                playerExecutes {
                    guiy(player) { CreativeMenu() }
                }
            }
            "cursor" {
                playerExecutes {
                    guiy(player) { Cursor() }
                }
            }
            "pagination" {
                playerExecutes {
                    guiy(player) { PaginatedMenu() }
                }
            }
            "scrolling" {
                playerExecutes {
                    guiy(player) { ScrollingMenu() }
                }
            }
            "anvil" {
                playerExecutes {
                    guiy(player) { AnvilGUI() }
                }
            }
        }
    }
}
