package com.solana.vendor.borshj

import java.io.Closeable
import java.io.Flushable
import java.io.IOException
import java.io.OutputStream
import java.util.*

class BorshWriter(stream: OutputStream) : BorshOutput<BorshWriter?>, Closeable, Flushable {
    protected val stream: OutputStream
    @Throws(IOException::class)
    override fun close() {
        stream.close()
    }

    @Throws(IOException::class)
    override fun flush() {
        stream.flush()
    }

    override fun write(array: ByteArray): BorshWriter {
        return try {
            stream.write(array)
            this
        } catch (error: IOException) {
            throw RuntimeException(error)
        }
    }

    override fun write(b: Byte): BorshWriter {
        return try {
            stream.write(b.toInt())
            this
        } catch (error: IOException) {
            throw RuntimeException(error)
        }
    }

    init {
        this.stream = Objects.requireNonNull(stream)
    }
}