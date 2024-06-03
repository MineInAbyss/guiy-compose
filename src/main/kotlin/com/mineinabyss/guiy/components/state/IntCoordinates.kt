package com.mineinabyss.guiy.components.state

@JvmInline
value class IntCoordinates(val pair: Long) {
    val x get() = (pair shr 32).toInt()
    val y get() = pair.toInt()

    operator fun component1() = x
    operator fun component2() = y

    constructor(x: Int, y: Int) : this((x.toLong() shl 32) or y.toLong())

    override fun toString(): String = "($x, $y)"

    operator fun plus(other: IntCoordinates) = IntCoordinates(x + other.x, y + other.y)
}

typealias IntOffset = IntCoordinates

@JvmInline
value class IntSize(val pair: Long) {
    val width get() = (pair shr 32).toInt()
    val height get() = pair.toInt()

    operator fun component1() = width
    operator fun component2() = height

    constructor(width: Int, height: Int) : this((width.toLong() shl 32) or height.toLong())

    override fun toString(): String = "($width, $height)"
}
