package com.solana.vendor.bip32.crypto

import com.solana.vendor.bip32.crypto.Hash.h160
import com.solana.vendor.bip32.crypto.Secp256k1.point
import com.solana.vendor.bip32.crypto.Secp256k1.serP
import java.math.BigInteger
import java.util.*

/**
 * General Util class for defined functions.
 */
object HdUtil {
    /**
     * ser32(i): serialize a 32-bit unsigned integer i as a 4-byte sequence,
     * most significant byte first.
     *
     *
     * Prefer long type to hold unsigned ints.
     *
     * @return ser32(i)
     */
    @JvmStatic
    fun ser32(i: Long): ByteArray {
        val ser = ByteArray(4)
        ser[0] = (i shr 24).toByte()
        ser[1] = (i shr 16).toByte()
        ser[2] = (i shr 8).toByte()
        ser[3] = i.toByte()
        return ser
    }

    /**
     * ser256(p): serializes the integer p as a 32-byte sequence, most
     * significant byte first.
     *
     * @param p big integer
     * @return 32 byte sequence
     */
    @JvmStatic
    fun ser256(p: BigInteger): ByteArray {
        val byteArray = p.toByteArray()
        val ret = ByteArray(32)

        //0 fill value
        Arrays.fill(ret, 0.toByte())

        //copy the bigint in
        if (byteArray.size <= ret.size) {
            System.arraycopy(byteArray, 0, ret, ret.size - byteArray.size, byteArray.size)
        } else {
            System.arraycopy(byteArray, byteArray.size - ret.size, ret, 0, ret.size)
        }
        return ret
    }

    /**
     * parse256(p): interprets a 32-byte sequence as a 256-bit number, most
     * significant byte first.
     *
     * @param p bytes
     * @return 256 bit number
     */
    @JvmStatic
    fun parse256(p: ByteArray): BigInteger {
        return BigInteger(1, p)
    }

    /**
     * Reverse given byte Array
     *
     * @param array an byte array
     */
    fun reverse(array: ByteArray) {
        var i = 0
        var j = array.size - 1
        var tmp: Byte
        while (j > i) {
            tmp = array[j]
            array[j] = array[i]
            array[i] = tmp
            j--
            i++
        }
    }

    /**
     * Append two byte arrays
     *
     * @param a first byte array
     * @param b second byte array
     * @return bytes appended
     */
    @JvmStatic
    fun append(a: ByteArray, b: ByteArray): ByteArray {
        val c = ByteArray(a.size + b.size)
        System.arraycopy(a, 0, c, 0, a.size)
        System.arraycopy(b, 0, c, a.size, b.size)
        return c
    }

    /**
     * Get the fingerprint
     *
     * @param keyData key data
     * @return fingerprint
     */
    @JvmStatic
    fun getFingerprint(keyData: ByteArray): ByteArray {
        val point = serP(point(parse256(keyData)))
        val h160 = h160(point)
        return byteArrayOf(h160[0], h160[1], h160[2], h160[3])
    }
}