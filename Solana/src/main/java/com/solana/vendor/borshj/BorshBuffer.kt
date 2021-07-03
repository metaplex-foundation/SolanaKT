package com.solana.vendor.borshj

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class BorshBuffer constructor(buffer: ByteBuffer) : BorshInput,
    BorshOutput<BorshBuffer?> {
    protected val buffer: ByteBuffer
    protected fun array(): ByteArray {
        assert(buffer.hasArray())
        return buffer.array()
    }

    fun toByteArray(): ByteArray {
        assert(buffer.hasArray())
        val arrayOffset = buffer.arrayOffset()
        return Arrays.copyOfRange(
            buffer.array(),
            arrayOffset, arrayOffset + buffer.position()
        )
    }

    fun capacity(): Int {
        return buffer.capacity()
    }

    fun reset(): BorshBuffer {
        buffer.reset()
        return this
    }

    override fun readU16(): Short {
        return buffer.short
    }

    override fun readU32(): Int {
        return buffer.int
    }

    override fun readU64(): Long {
        return buffer.long
    }

    override fun readF32(): Float {
        return buffer.float
    }

    override fun readF64(): Double {
        return buffer.double
    }

    override fun read(): Byte {
        return buffer.get()
    }

    override fun read(result: ByteArray, offset: Int, length: Int) {
        buffer[result, offset, length]
    }

    override fun writeU16(value: Short): BorshBuffer {
        buffer.putShort(value)
        return this
    }

    override fun writeU32(value: Int): BorshBuffer {
        buffer.putInt(value)
        return this
    }

    override fun writeU64(value: Long): BorshBuffer {
        buffer.putLong(value)
        return this
    }

    override fun writeF32(value: Float): BorshBuffer {
        buffer.putFloat(value)
        return this
    }

    override fun writeF64(value: Double): BorshBuffer {
        buffer.putDouble(value)
        return this
    }

    override fun write(bytes: ByteArray): BorshBuffer {
        buffer.put(bytes)
        return this
    }

    override fun write(b: Byte): BorshBuffer {
        buffer.put(b)
        return this
    }

    companion object {
        @JvmStatic
        fun allocate(capacity: Int): BorshBuffer {
            return BorshBuffer(ByteBuffer.allocate(capacity))
        }

        fun allocateDirect(capacity: Int): BorshBuffer {
            return BorshBuffer(ByteBuffer.allocateDirect(capacity))
        }

        @JvmStatic
        fun wrap(array: ByteArray?): BorshBuffer {
            return BorshBuffer(ByteBuffer.wrap(array))
        }
    }

    init {
        this.buffer = Objects.requireNonNull(buffer)
        this.buffer.order(ByteOrder.LITTLE_ENDIAN)
        this.buffer.mark()
    }
}