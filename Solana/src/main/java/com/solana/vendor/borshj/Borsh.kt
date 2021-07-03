package com.solana.vendor.borshj

import com.solana.vendor.borshj.BorshBuffer.Companion.allocate
import com.solana.vendor.borshj.BorshBuffer.Companion.wrap
import java.util.*

interface Borsh {
    companion object{
        @JvmStatic
        fun <T> isSerializable(klass: Class<T>?): Boolean {
            return if (klass == null) false else Arrays.stream(klass.interfaces)
                .anyMatch { iface: Class<*> -> iface == Borsh::class.java } ||
                    isSerializable(klass.superclass)
        }

        @JvmStatic
        fun serialize(obj: Any): ByteArray {
            return allocate(4096).write(obj)!!.toByteArray()
        }

        @JvmStatic
        fun <T> deserialize(bytes: ByteArray, klass: Class<T>): T {
            return deserialize(wrap(bytes), klass)
        }

        private fun <T> deserialize(buffer: BorshBuffer, klass: Class<*>): T {
            return buffer.read(klass)
        }
    }

}