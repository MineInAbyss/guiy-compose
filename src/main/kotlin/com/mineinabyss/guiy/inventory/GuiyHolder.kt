package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.snapshots.Snapshot
import com.mineinabyss.guiy.nodes.BoxNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import kotlinx.coroutines.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class GuiyHolder(
    override val height: Int,
    val title: Component = Component.text("")
) : GuiyCanvas, InventoryHolder {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }
    val composeScope = CoroutineScope(Dispatchers.Default) + clock
    val composeContext = composeScope.coroutineContext


    private val inventory: Inventory = Bukkit.createInventory(this, INVENTORY_WIDTH * height, title)
    override fun getInventory(): Inventory = inventory

    override val width = INVENTORY_WIDTH

    override fun set(x: Int, y: Int, item: ItemStack?) {
        inventory.setItem(y * INVENTORY_WIDTH + x, item)
    }

    override fun clear(left: Int, top: Int, right: Int, bottom: Int) {
        for (x in left..right)
            for (y in top..bottom)
                inventory.clear(y * INVENTORY_WIDTH + x)
    }


    var running = false

    private val rootNode = BoxNode()
    private val recomposer = Recomposer(composeContext)
    private val composition = Composition(GuiyNodeApplier(rootNode), recomposer)

    fun stop() {
        recomposer.close()
        inventory.close()
    }

    val scope = object : GuiyScope {}

    fun start() {
        !running || return
        running = true

        composeScope.apply {
            launch(composeContext) {
                recomposer.runRecomposeAndApplyChanges()
            }
            launch(composeContext) {
                while (running) {
                    if (hasFrameWaiters) {
                        hasFrameWaiters = false
                        clock.sendFrame(0L) // Frame time value is not used by Compose runtime.

                        clear()
                        rootNode.render(this@GuiyHolder)
                    }
                    delay(50)
                }
            }

            launch(composeContext) {
                var applyScheduled = false
                val snapshotHandle = Snapshot.registerGlobalWriteObserver {
                    if (!applyScheduled) {
                        applyScheduled = true
                        composeScope.launch {
                            applyScheduled = false
                            Snapshot.sendApplyNotifications()
                        }
                    }
                }
                try {
                    recomposer.join()
                } finally {
                    recomposer.close()
                    snapshotHandle.dispose()
                    composition.dispose()
                }
            }
        }
    }

    fun setContent(content: @Composable GuiyScope.() -> Unit) {
        hasFrameWaiters = true
        composition.setContent {
            scope.content()
        }
    }
}

interface GuiyScope

fun guiy(
    height: Int = 6,
    title: Component = Component.text(""),
    content: @Composable GuiyScope.() -> Unit
): GuiyHolder {
    return GuiyHolder(height, title).apply {
        start()
        setContent(content)
    }
}
