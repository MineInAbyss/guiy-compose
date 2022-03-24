package com.mineinabyss.guiy.navigation

import net.wesjd.anvilgui.AnvilGUI

sealed class UniversalScreens {
    class Anvil(val builder: AnvilGUI.Builder): UniversalScreens()
}
