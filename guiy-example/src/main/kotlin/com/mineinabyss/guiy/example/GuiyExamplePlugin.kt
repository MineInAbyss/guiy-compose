package com.mineinabyss.guiy.example

import com.mineinabyss.guiy.canvas.guiy
import com.mineinabyss.guiy.example.gui.*
import com.mineinabyss.idofront.commands.brigadier.commands
import org.bukkit.plugin.java.JavaPlugin

class GuiyExamplePlugin : JavaPlugin() {
    override fun onEnable() = commands {
        "guiyexample" {
            "arrangement" {
                executes.asPlayer {
                    guiy(player) { ArrangementMenu() }
                }
            }
            "animated" {
                executes.asPlayer {
                    guiy(player) { AnimatedTitle() }
                }
            }
            "autoReopen" {
                executes.asPlayer {
                    guiy(player) { AutoReopen() }
                }
            }
            "creative" {
                executes.asPlayer {
                    guiy(player) { CreativeMenu() }
                }
            }
            "cursor" {
                executes.asPlayer {
                    guiy(player) { Cursor() }
                }
            }
            "pagination" {
                executes.asPlayer {
                    guiy(player) { PaginatedMenu() }
                }
            }
            "scrolling" {
                executes.asPlayer {
                    guiy(player) { ScrollingMenu() }
                }
            }
            "anvil" {
                executes.asPlayer {
                    guiy(player) { AnvilGUI() }
                }
            }
        }
    }
}
