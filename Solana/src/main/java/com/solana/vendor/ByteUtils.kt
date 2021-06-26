package com.solana.vendor

import okhttp3.internal.and
import org.bitcoinj.core.Utils
import java.io.IOException
import java.io.OutputStream
import java.math.BigInteger
import java.util.*

object ByteUtils {
    const val UINT_32_LENGTH = 4
    const val UINT_64_LENGTH = 8
    val HEX_ARRAY = "0123456789ABCDEF".toCharArray()
    @JvmStatic
    fun readBytes(buf: ByteArray?, offset: Int, length: Int): ByteArray {
        val b = ByteArray(length)
        System.arraycopy(buf, offset, b, 0, length)
        return b
    }

    @JvmStatic
    fun readUint64(buf: ByteArray?, offset: Int): BigInteger {
        return BigInteger(Utils.reverseBytes(readBytes(buf, offset, UINT_64_LENGTH)))
    }

    fun readUint64Price(buf: ByteArray?, offset: Int): BigInteger {
        return BigInteger(readBytes(buf, offset, UINT_64_LENGTH))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun uint64ToByteStreamLE(`val`: BigInteger, stream: OutputStream) {
        var bytes = `val`.toByteArray()
        if (bytes.size > 8) {
            bytes = if (bytes[0].equals(0)) {
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

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v: Int = bytes[j].and(0xFF)
            hexChars[j * 2] = HEX_ARRAY[v ushr 4]
            hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
        }
        return String(hexChars)
    }

    fun trim(bytes: ByteArray): ByteArray {
        var i = bytes.size - 1
        while (i >= 0 && bytes[i].equals(0)) {
            --i
        }
        return Arrays.copyOf(bytes, i + 1)
    }

    fun getBit(data: ByteArray, pos: Int): Int {
        val posByte = pos / 8
        val posBit = pos % 8
        val valByte = data[posByte].toInt()
        return (valByte shr posBit) and 1
    }
}