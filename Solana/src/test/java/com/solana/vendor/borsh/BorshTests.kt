package com.solana.vendor.borsh

import com.solana.core.PublicKeyRule
import com.solana.models.Buffer.AccountInfoRule
import com.solana.models.Buffer.MintRule
import com.solana.models.Buffer.TokenSwapInfoRule
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshCodable
import org.junit.Assert
import org.junit.Test

class BorshTests {
    private val borsh = Borsh()
    @Test
    fun roundtripPoint2Df() {
        val point = Point2Df(123f, 456f)
        Assert.assertEquals(point, borsh.deserialize(borsh.serialize(point), Point2Df::class.java))
    }

    @Test
    fun roundtripRect2Df() {
        val topLeft = Point2Df(-123f, -456f)
        val bottomRight = Point2Df(123f, 456f)
        val rect = Rect2Df(topLeft, bottomRight)
        Assert.assertEquals(rect, borsh.deserialize(borsh.serialize(rect), Rect2Df::class.java))
    }

    class Point2Df : BorshCodable {
        private var x = 0f
        private var y = 0f

        constructor(x: Float, y: Float) {
            this.x = x
            this.y = y
        }

        override fun toString(): String {
            return String.format("Point2Df(%f, %f)", x, y)
        }

        override fun equals(obj: Any?): Boolean {
            if (obj == null || obj.javaClass != this.javaClass) return false
            val other = obj as Point2Df
            return x == other.x && y == other.y
        }
    }

    class Rect2Df : BorshCodable {
        private var topLeft: Point2Df? = null
        private var bottomRight: Point2Df? = null

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