package com.mineinabyss.guiy.components.state

@JvmInline
value class IntCoordinates(private val pair: Long) {
    val x get() = (pair shr 32).toInt()
    val y get() = pair.toInt()

    operator fun component1() = x
    operator fun component2() = y

    constructor(x: Int, y: Int) : this((x.toLong() shl 32) or y.toLong())
}
