package com.solana.vendor.borshj

import com.solana.vendor.borshj.BorshBuffer.Companion.allocate
import com.solana.vendor.borshj.BorshBuffer.Companion.wrap
import java.util.*

interface BorshCodable

interface BorshRule<T> {
    val clazz: Class<T>
    fun read(input: BorshInput): T?
    fun <Self> write(obj: Any, output: BorshOutput<Self>): Self
}

class Borsh {
    private var rules: List<BorshRule<*>> = listOf()

    fun setRules(rules: List<BorshRule<*>>) {
        this.rules = rules
    }

    fun getRules(): List<BorshRule<*>> {
        return rules
    }

    fun <T> isSerializable(klass: Class<T>?): Boolean {
        return if (klass == null) {
            false
        } else {
            Arrays.stream(klass.interfaces)
                .anyMatch {
                        iface: Class<*> -> iface == BorshCodable::class.java
                } || isSerializable(klass.superclass)
        }
    }

    fun serialize(obj: Any): ByteArray {
        return allocate(4096).write(this, obj)!!.toByteArray()
    }

    fun <T> deserialize(bytes: ByteArray, klass: Class<T>): T {
        return deserialize(wrap(bytes), klass)
    }

    private fun <T> deserialize(buffer: BorshBuffer, klass: Class<*>): T {
        return buffer.read(this, klass)
    }
}