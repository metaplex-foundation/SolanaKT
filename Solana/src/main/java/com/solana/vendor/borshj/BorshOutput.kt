package com.solana.vendor.borshj

import com.solana.vendor.borshj.BorshBuffer.Companion.allocate
import java.lang.reflect.Modifier
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.*

interface BorshOutput<Self> {
    fun write(obj: Any): Self {
        if (obj is Byte) {
            return this.writeU8(obj)
        } else if (obj is Short) {
            return this.writeU16(obj)
        } else if (obj is Int) {
            return writeU32(obj)
        } else if (obj is Long) {
            return writeU64(obj)
        } else if (obj is Float) {
            return writeF32(obj)
        } else if (obj is Double) {
            return writeF64(obj)
        } else if (obj is BigInteger) {
            return this.writeU128(obj)
        } else if (obj is String) {
            return writeString(obj)
        } else if (obj is ByteArray) {
            return this.writeFixedArray(obj)
        } else if (obj is List<*>) {
            return this.writeArray(obj) as Self
        } else if (obj is Boolean) {
            return writeBoolean<Any>(obj)
        } else if (obj is Optional<*>) {
            return writeOptional(obj)
        } else if (obj is Borsh) {
            return writePOJO(obj)
        }
        throw IllegalArgumentException()
    }

    fun writePOJO(obj: Borsh): Self {
        try {
            val fields = obj.javaClass.declaredFields
                .filter { it.name != "Companion" }
                .filter { !Modifier.isStatic(it.modifiers) }
            for (field in fields) {
                field.isAccessible = true
                this.write(field[obj])
            }
        } catch (error: IllegalAccessException) {
            throw RuntimeException(error)
        }
        return this as Self
    }

    fun writeU8(value: Int): Self {
        return this.writeU8(value.toByte())
    }

    fun writeU8(value: Byte): Self {
        return this.write(value)
    }

    fun writeU16(value: Int): Self {
        return this.writeU16(value.toShort())
    }

    fun writeU16(value: Short): Self {
        return writeBuffer(allocate(2).writeU16(value))
    }

    fun writeU32(value: Int): Self {
        return writeBuffer(allocate(4).writeU32(value))
    }

    fun writeU64(value: Long): Self {
        return writeBuffer(allocate(8).writeU64(value))
    }

    fun writeU128(value: Long): Self {
        return this.writeU128(BigInteger.valueOf(value))
    }

    fun writeU128(value: BigInteger): Self {
        if (value.signum() == -1) {
            throw ArithmeticException("integer underflow")
        }
        if (value.bitLength() > 128) {
            throw ArithmeticException("integer overflow")
        }
        val bytes = value.toByteArray()
        for (i in bytes.indices.reversed()) {
            this.write(bytes[i])
        }
        for (i in 0 until 16 - bytes.size) {
            this.write(0.toByte())
        }
        return this as Self
    }

    fun writeF32(value: Float): Self {
        return writeBuffer(allocate(4).writeF32(value))
    }

    fun writeF64(value: Double): Self {
        return writeBuffer(allocate(8).writeF64(value))
    }

    fun writeString(string: String): Self {
        val bytes = string.toByteArray(StandardCharsets.UTF_8)
        writeU32(bytes.size)
        return this.write(bytes)
    }

    fun writeFixedArray(array: ByteArray): Self {
        return this.write(array)
    }

    fun <T> writeArray(array: Array<T>): Self {
        writeU32(array.size)
        for (element in array) {
            this.write(element!!)
        }
        return this as Self
    }

    fun <T> writeArray(list: List<T>): Self {
        writeU32(list.size)
        for (element in list) {
            this.write(element!!)
        }
        return this as Self
    }

    fun <T> writeBoolean(value: Boolean): Self {
        return this.writeU8(if (value) 1 else 0)
    }

    fun <T> writeOptional(optional: Optional<T>): Self {
        return if (optional.isPresent) {
            this.writeU8(1)
            this.write(optional.get()!!)
        } else {
            this.writeU8(0)
        }
    }

    fun writeBuffer(buffer: BorshBuffer): Self {
        return this.write(buffer.toByteArray()) // TODO: optimize
    }

    fun write(bytes: ByteArray): Self
    fun write(b: Byte): Self
}