package com.solana.vendor.bip32.crypto

import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Utility class for Hmac SHA-512
 */
object HmacSha512 {
    private const val HMAC_SHA512 = "HmacSHA512"

    /**
     * hmac512
     *
     * @param key key
     * @param seed seed
     * @return hmac512
     */
    @JvmStatic
    fun hmac512(key: ByteArray, seed: ByteArray): ByteArray {
        return try {
            val sha512_HMAC = Mac.getInstance(HMAC_SHA512)
            val keySpec = SecretKeySpec(seed, HMAC_SHA512)
            sha512_HMAC.init(keySpec)
            sha512_HMAC.doFinal(key)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Unable to perform hmac512.", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Unable to perform hmac512.", e)
        }
    }
}