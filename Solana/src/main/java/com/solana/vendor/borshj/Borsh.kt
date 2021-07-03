package com.solana.vendor.borshj

import com.solana.vendor.borshj.BorshBuffer.Companion.allocate
import com.solana.vendor.borshj.BorshBuffer.Companion.wrap
import java.util.*

interface Borsh {
    companion object {
        @JvmStatic
        fun serialize(`object`: Any): ByteArray {
            return allocate(4096).write(Objects.requireNonNull(`object`))!!.toByteArray()
        }

        @JvmStatic
        fun <T> deserialize(bytes: ByteArray, klass: Class<*>): T {
            return deserialize(wrap(Objects.requireNonNull(bytes)), klass)
        }

        private fun <T> deserialize(buffer: BorshBuffer, klass: Class<*>): T {
            return buffer.read(Objects.requireNonNull(klass))
        }

        @JvmStatic
        fun isSerializable(klass: Class<*>?): Boolean {
            return if (klass == null) false else Arrays.stream(klass.interfaces)
                .anyMatch { iface: Class<*> -> iface == Borsh::class.java } ||
                    isSerializable(klass.superclass)
        }
    }
}