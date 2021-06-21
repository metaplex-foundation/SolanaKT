package com.solana.vendor

import org.bitcoinj.core.Utils
import java.io.IOException
import java.io.OutputStream
import java.math.BigInteger

object ByteUtils {
    const val UINT_32_LENGTH = 4
    const val UINT_64_LENGTH = 8
    fun readBytes(buf: ByteArray?, offset: Int, length: Int): ByteArray {
        val b = ByteArray(length)
        System.arraycopy(buf, offset, b, 0, length)
        return b
    }

    fun readUint64(buf: ByteArray?, offset: Int): BigInteger {
        return BigInteger(Utils.reverseBytes(readBytes(buf, offset, UINT_64_LENGTH)))
    }

    @Throws(IOException::class)
    fun uint64ToByteStreamLE(`val`: BigInteger, stream: OutputStream) {
        var bytes = `val`.toByteArray()
        if (bytes.size > 8) {
            bytes = if (bytes[0] == 0.toByte()) {
                readBytes(bytes, 1, bytes.size - 1)
            } else {
                throw RuntimeException("Input too large to encode into a uint64")
            }
        }
        bytes = Utils.reverseBytes(bytes)
        stream.write(bytes)
        if (bytes.size < 8) {
            for (i in 0 until 8 - bytes.size) stream.write(0)
        }
    }
}