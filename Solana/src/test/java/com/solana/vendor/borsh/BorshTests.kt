package com.solana.vendor.borsh

import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.Borsh.Companion.deserialize
import com.solana.vendor.borshj.Borsh.Companion.serialize
import org.junit.Assert
import org.junit.Test

class BorshTests {
    @Test
    fun roundtripPoint2Df() {
        val point = Point2Df(123f, 456f)
        Assert.assertEquals(point, deserialize(serialize(point), Point2Df::class.java))
    }

    @Test
    fun roundtripRect2Df() {
        val topLeft = Point2Df(-123f, -456f)
        val bottomRight = Point2Df(123f, 456f)
        val rect = Rect2Df(topLeft, bottomRight)
        Assert.assertEquals(rect, deserialize(serialize(rect), Rect2Df::class.java))
    }

    class Point2Df : Borsh {
        private var x = 0f
        private var y = 0f

        constructor() {}
        constructor(x: Float, y: Float) {
            this.x = x
            this.y = y
        }

        override fun toString(): String {
            return String.format("Point2Df(%f, %f)", x, y)
        }

        override fun equals(`object`: Any?): Boolean {
            if (`object` == null || `object`.javaClass != this.javaClass) return false
            val other = `object` as Point2Df
            return x == other.x && y == other.y
        }
    }

    class Rect2Df : Borsh {
        private var topLeft: Point2Df? = null
        private var bottomRight: Point2Df? = null

        constructor() {}
        constructor(topLeft: Point2Df?, bottomRight: Point2Df?) {
            this.topLeft = topLeft
            this.bottomRight = bottomRight
        }

        override fun toString(): String {
            return String.format("Rect2Df(%s, %s)", topLeft.toString(), bottomRight.toString())
        }

        override fun equals(`object`: Any?): Boolean {
            if (`object` == null || `object`.javaClass != this.javaClass) return false
            val other = `object` as Rect2Df
            return topLeft == other.topLeft && bottomRight == other.bottomRight
        }
    }
}