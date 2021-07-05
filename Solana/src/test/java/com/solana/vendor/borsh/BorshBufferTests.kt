package com.solana.vendor.borsh

import com.solana.core.PublicKeyRule
import com.solana.models.Buffer.AccountInfoRule
import com.solana.models.Buffer.MintRule
import com.solana.models.Buffer.TokenSwapInfoRule
import com.solana.vendor.borshj.Borsh
import com.solana.vendor.borshj.BorshBuffer
import com.solana.vendor.borshj.BorshBuffer.Companion.allocate
import com.solana.vendor.borshj.BorshBuffer.Companion.wrap
import org.junit.Assert
import org.junit.Test
import java.math.BigInteger
import java.util.*

class BorshBufferTests {

    fun borsh(): Borsh {
        val borsh = Borsh()
        return borsh
    }

    private var buffer: BorshBuffer? = null
    fun newBuffer() {
        buffer = allocate(256)
    }

    @Test
    fun readU8() {
        newBuffer()
        buffer = wrap(byteArrayOf(0x42))
        Assert.assertEquals(0x42, buffer!!.readU8().toLong())
    }

    @Test
    fun readU16() {
        newBuffer()
        buffer = wrap(byteArrayOf(0x11, 0x00))
        Assert.assertEquals(0x0011, buffer!!.readU16().toLong())
    }

    @Test
    fun readU32() {
        newBuffer()
        buffer = wrap(byteArrayOf(0x33, 0x22, 0x11, 0x00))
        Assert.assertEquals(0x00112233, buffer!!.readU32().toLong())
    }

    @Test
    fun readU64() {
        newBuffer()
        buffer = wrap(byteArrayOf(0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00))
        Assert.assertEquals(0x0011223344556677L, buffer!!.readU64())
    }

    @Test
    fun readU128() {
        newBuffer()
        val input = byteArrayOf(
            0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        )
        buffer = wrap(input)
        Assert.assertEquals(BigInteger.valueOf(0x0011223344556677L), buffer!!.readU128())
    }

    @Test
    fun readF32() {
        newBuffer()
        Assert.assertEquals(0.0, wrap(byteArrayOf(0, 0, 0, 0)).readF32().toDouble(), 0.0)
        Assert.assertEquals(
            1.0,
            wrap(byteArrayOf(0, 0, 0x80.toByte(), 0x3f.toByte())).readF32().toDouble(),
            0.0
        )
    }

    @Test
    fun readF64() {
        newBuffer()
        Assert.assertEquals(0.0, wrap(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)).readF64(), 0.0)
        Assert.assertEquals(
            1.0,
            wrap(byteArrayOf(0, 0, 0, 0, 0, 0, 0xf0.toByte(), 0x3f.toByte())).readF64(),
            0.0
        )
    }

    @Test
    fun readString() {
        newBuffer()
        val input = byteArrayOf(
            5,
            0,
            0,
            0,
            'B'.code.toByte(),
            'o'.code.toByte(),
            'r'.code.toByte(),
            's'.code.toByte(),
            'h'.code.toByte()
        )
        buffer = wrap(input)
        Assert.assertEquals("Borsh", buffer!!.readString())
    }

    @Test
    fun readFixedArray() {
        newBuffer()
        val input = byteArrayOf(1, 2, 3, 4, 5)
        buffer = wrap(input)
        Assert.assertEquals(0, buffer!!.reset().readFixedArray(0).size.toLong())
        Assert.assertEquals(1, buffer!!.reset().readFixedArray(1).size.toLong())
        Assert.assertEquals(5, buffer!!.reset().readFixedArray(5).size.toLong())
        Assert.assertArrayEquals(input, buffer!!.reset().readFixedArray(5))
    }

    @Test
    fun readArray() {
        newBuffer()
        val input = byteArrayOf(3, 0, 0, 0, 1, 0, 2, 0, 3, 0)
        buffer = wrap(input)
        Assert.assertArrayEquals(
            arrayOf<Short>(1, 2, 3), buffer!!.readArray(
                borsh(),
                Short::class.javaObjectType
            )
        )
    }

    @Test
    fun readBoolean() {
        newBuffer()
        Assert.assertFalse(wrap(byteArrayOf(0)).readBoolean())
        Assert.assertTrue(wrap(byteArrayOf(1)).readBoolean())
    }

    @Test
    fun readOptional() {
        newBuffer()
        Assert.assertEquals(Optional.empty<Any>(), wrap(byteArrayOf(0)).readOptional<Any>())
        Assert.assertEquals(
            Optional.of(42), wrap(byteArrayOf(1, 42, 0, 0, 0)).readOptional<Any>(
                borsh(),
                Int::class.java
            )
        )
    }

    @Test
    fun writeU8() {
        newBuffer()
        val actual = buffer!!.writeU8(0x42)!!.toByteArray()
        val expected = byteArrayOf(0x42)
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeU16() {
        newBuffer()
        val actual = buffer!!.writeU16(0x0011)!!.toByteArray()
        val expected = byteArrayOf(0x11, 0x00)
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeU32() {
        newBuffer()
        val actual = buffer!!.writeU32(0x00112233).toByteArray()
        val expected = byteArrayOf(0x33, 0x22, 0x11, 0x00)
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeU64() {
        newBuffer()
        val actual = buffer!!.writeU64(0x0011223344556677L).toByteArray()
        val expected = byteArrayOf(
            0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00
        )
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeU128() {
        newBuffer()
        val actual = buffer!!.writeU128(0x0011223344556677L)!!.toByteArray()
        val expected = byteArrayOf(
            0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        )
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeF32() {
        newBuffer()
        val actual = buffer!!.writeF32(1.0f).toByteArray()
        val expected = byteArrayOf(0, 0, 0x80.toByte(), 0x3f.toByte())
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeF64() {
        newBuffer()
        val actual = buffer!!.writeF64(1.0).toByteArray()
        val expected = byteArrayOf(0, 0, 0, 0, 0, 0, 0xf0.toByte(), 0x3f.toByte())
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeString() {
        newBuffer()
        val actual = buffer!!.writeString("Borsh")!!.toByteArray()
        val expected = byteArrayOf(
            5,
            0,
            0,
            0,
            'B'.code.toByte(),
            'o'.code.toByte(),
            'r'.code.toByte(),
            's'.code.toByte(),
            'h'.code.toByte()
        )
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeFixedArray() {
        newBuffer()
        buffer!!.writeFixedArray(byteArrayOf(1, 2, 3, 4, 5))
        val expected = byteArrayOf(1, 2, 3, 4, 5)
        val actual = buffer!!.toByteArray()
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeArray() {
        newBuffer()
        buffer!!.writeArray(borsh(), arrayOf<Short>(1, 2, 3))
        val expected = byteArrayOf(3, 0, 0, 0, 1, 0, 2, 0, 3, 0)
        val actual = buffer!!.toByteArray()
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeArrayOfList() {
        newBuffer()
        buffer!!.writeArray(borsh(), listOf(*arrayOf<Short>(1, 2, 3)))
        val expected = byteArrayOf(3, 0, 0, 0, 1, 0, 2, 0, 3, 0)
        val actual = buffer!!.toByteArray()
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun writeBoolean() {
        newBuffer()
        Assert.assertArrayEquals(
            byteArrayOf(0), buffer!!.reset().writeBoolean<Any>(false)!!
                .toByteArray()
        )
        Assert.assertArrayEquals(
            byteArrayOf(1), buffer!!.reset().writeBoolean<Any>(true)!!
                .toByteArray()
        )
    }

    @Test
    fun writeOptional() {
        newBuffer()
        Assert.assertArrayEquals(
            byteArrayOf(0), buffer!!.reset().writeOptional(borsh(), Optional.empty<Any>())!!
                .toByteArray()
        )
        Assert.assertArrayEquals(
            byteArrayOf(1, 42, 0, 0, 0), buffer!!.reset().writeOptional(
                borsh(),
                Optional.of(42)
            )!!
                .toByteArray()
        )
    }

    @Test
    fun testF32() {
        newBuffer()
        val value = 3.1415f
        Assert.assertEquals(
            value.toDouble(),
            wrap(buffer!!.writeF32(value).toByteArray()).readF32().toDouble(),
            0.0
        )
    }

    @Test
    fun testF64() {
        newBuffer()
        val value = 3.1415
        Assert.assertEquals(value, wrap(buffer!!.writeF64(value).toByteArray()).readF64(), 0.0)
    }
}