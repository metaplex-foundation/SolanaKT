package com.solana.vendor.borshj

import com.solana.vendor.borshj.BorshBuffer.Companion.allocate
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.*

interface BorshOutput<Self> {
    fun write(borsh: Borsh, obj: Any): Self {
        val rule: BorshRule<*>? = borsh.getRules().firstOrNull { it.clazz == obj.javaClass }

        return when {
            rule != null -> rule.write(obj, this)
            obj is Byte -> writeU8(obj)
            obj is Short -> writeU16(obj)
            obj is Int -> writeU32(obj)
            obj is Long -> writeU64(obj)
            obj is Float -> writeF32(obj)
            obj is Double -> writeF64(obj)
            obj is BigInteger -> writeU128(obj)
            obj is String -> writeString(obj)
            obj is ByteArray -> writeFixedArray(obj)
            obj is List<*> -> writeArray(borsh, obj)
            obj is Boolean -> writeBoolean<Any>(obj)
            obj is Optional<*> -> writeOptional(borsh, obj)
            obj is BorshCodable -> writePOJO(borsh, obj)
            else -> throw IllegalArgumentException()
        }
    }

    fun writePOJO(borsh: Borsh, obj: BorshCodable): Self {
        try {
            val constructor = obj.javaClass.kotlin.constructors.first()
            val kotlinParameters = constructor.parameters

            val fields = obj.javaClass.declaredFields
                .filter { kotlinParameters.map { kf -> kf.name }.contains(it.name) }
                .sortedBy { field -> field.getAnnotation(FieldOrder::class.java).order }

            for (field in fields) {
                field.isAccessible = true
                this.write(borsh, field[obj])
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

    fun <T> writeArray(borsh: Borsh, array: Array<T>): Self {
        writeU32(array.size)
        for (element in array) {
            this.write(borsh, element!!)
        }
        return this as Self
    }

    fun <T> writeArray(borsh: Borsh, list: List<T>): Self {
        writeU32(list.size)
        for (element in list) {
            this.write(borsh, element!!)
        }
        return this as Self
    }

    fun <T> writeBoolean(value: Boolean): Self {
        return this.writeU8(if (value) 1 else 0)
    }

    fun writeNothing(): Self {
        return this.writeFixedArray(ByteArray(0))
    }

    fun <T> writeOptional(borsh: Borsh, optional: Optional<T>): Self {
        return if (optional.isPresent) {
            this.writeU8(1)
            this.write(borsh, optional.get()!!)
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