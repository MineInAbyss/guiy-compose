package com.mineinabyss.guiy.canvas

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.mineinabyss.guiy.guiyPlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.newSingleThreadContext
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A [CoroutineDispatcher] that runs on a single thread and acts like `Dispatchers.Main.immediate` from other compose
 * platforms (i.e. it won't redispatch if already on the correct thread). Uses a [newSingleThreadContext] under the hood.
 */
class GuiyUIDispatcher() : CoroutineDispatcher(), Closeable {
    private var uiThread: Thread? = null
    private val dispatcher = newSingleThreadContext("Guiy UI Thread")

    init {
        //TODO any better way of doing this?
        dispatcher.dispatch(EmptyCoroutineContext) { uiThread = Thread.currentThread() }
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return Thread.currentThread() != uiThread
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) = dispatcher.dispatch(context, block)

    override fun close() = dispatcher.close()

    //TODO Ideally don't use a single dispatcher for all recomposition jobs, however we want to reference this in ViewModels
    // and Snapshots have a lot of global state which makes me afraid to run multiple threads here.
    companion object {
        /**
         * A main dispatcher that will close when the Guiy plugin is disabled.
         *
         * All UI operations should run on this dispatcher, including recompositions and [com.mineinabyss.guiy.viewmodel.GuiyViewModel]'s viewModelScope.
         */
        val Main = guiyPlugin.minecraftDispatcher + GuiyUIDispatcher()
    }
}