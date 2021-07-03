package com.solana.vendor.borshj

import java.io.Closeable
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.util.*

class BorshReader(stream: InputStream) : BorshInput, Closeable {
    private val stream: InputStream = Objects.requireNonNull(stream)

    @Throws(IOException::class)
    override fun close() {
        stream.close()
    }

    override fun read(): Byte {
        return try {
            val result = stream.read()
            if (result == -1) {
                throw EOFException()
            }
            result.toByte()
        } catch (error: IOException) {
            throw RuntimeException(error)
        }
    }

    override fun read(result: ByteArray, offset: Int, length: Int) {
        if (offset < 0 || length < 0 || length > result.size - offset) {
            throw IndexOutOfBoundsException()
        }
        try {
            var n = 0
            while (n < length) {
                val count = stream.read(result, offset + n, length - n)
                if (count == -1) {
                    throw EOFException()
                }
                n += count
            }
        } catch (error: IOException) {
            throw RuntimeException(error)
        }
    }

}