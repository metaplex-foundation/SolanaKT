package com.solana.vendor.bip32.crypto

import org.bouncycastle.crypto.digests.RIPEMD160Digest
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Basic hash functions
 */
object Hash {
    /**
     * SHA-256
     *
     * @param input input
     * @return sha256(input)
     */
    @JvmStatic
    fun sha256(input: ByteArray): ByteArray {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.digest(input)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Unable to find SHA-256", e)
        }
    }

    /**
     * sha256(sha256(bytes))
     *
     * @param bytes input
     * @return sha'd twice result
     */
    @JvmStatic
    fun sha256Twice(bytes: ByteArray, offset: Int = 0, length: Int = bytes.size): ByteArray {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(bytes, offset, length)
            digest.update(digest.digest())
            digest.digest()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Unable to find SHA-256", e)
        }
    }

    /**
     * H160
     *
     * @param input input
     * @return h160(input)
     */
    @JvmStatic
    fun h160(input: ByteArray): ByteArray {
        val sha256 = sha256(input)
        val digest = RIPEMD160Digest()
        digest.update(sha256, 0, sha256.size)
        val out = ByteArray(20)
        digest.doFinal(out, 0)
        return out
    }
}