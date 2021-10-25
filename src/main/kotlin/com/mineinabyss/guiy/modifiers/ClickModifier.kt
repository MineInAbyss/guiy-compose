package com.mineinabyss.guiy.modifiers

class ClickModifier(val onClick: (() -> Unit)) {
}

override fun processClick(x: Int, y: Int) {
    super.processClick(x, y)
    children.firstOrNull { x in it.x until (it.x + it.width) && y in it.y until (it.y + it.height) }
        ?.processClick(x, y)
}
