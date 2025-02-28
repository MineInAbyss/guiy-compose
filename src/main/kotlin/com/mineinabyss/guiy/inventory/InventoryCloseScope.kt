package com.mineinabyss.guiy.inventory

import androidx.compose.runtime.Stable

@Stable
interface InventoryCloseScope {
    fun reopen()
    fun exit()
}
