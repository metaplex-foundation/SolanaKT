package com.solana.models

import com.solana.vendor.ByteUtils
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import org.bitcoinj.core.Sha256Hash
import java.io.ByteArrayOutputStream
import java.util.*

class PublicKey {
    private var pubkey: ByteArray

    constructor(pubkey: String) {
        require(pubkey.length >= PUBLIC_KEY_LENGTH) { "Invalid public key input" }
        this.pubkey = Base58.decode(pubkey)
    }

    constructor(pubkey: ByteArray) {
        require(pubkey.size <= PUBLIC_KEY_LENGTH) { "Invalid public key input" }
        this.pubkey = pubkey
    }

    fun toByteArray(): ByteArray {
        return pubkey
    }

    fun toBase58(): String {
        return Base58.encode(pubkey)
    }

    fun equals(pubkey: PublicKey): Boolean {
        return Arrays.equals(this.pubkey, pubkey.toByteArray())
    }

    override fun toString(): String {
        return toBase58()
    }

    class ProgramDerivedAddress(val address: PublicKey, val nonce: Int)
    companion object {
        const val PUBLIC_KEY_LENGTH = 32
        fun readPubkey(bytes: ByteArray?, offset: Int): PublicKey {
            val buf = ByteUtils.readBytes(bytes, offset, PUBLIC_KEY_LENGTH)
            return PublicKey(buf)
        }

        @Throws(Exception::class)
        fun createProgramAddress(seeds: List<ByteArray>, programId: PublicKey): PublicKey {
            val buffer = ByteArrayOutputStream()
            for (seed in seeds) {
                require(seed.size <= 32) { "Max seed length exceeded" }
                buffer.write(seed)
            }
            buffer.write(programId.toByteArray())
            buffer.write("ProgramDerivedAddress".toByteArray())
            val hash = Sha256Hash.hash(buffer.toByteArray())
            if (TweetNaclFast.is_on_curve(hash) != 0) {
                throw Exception("Invalid seeds, address must fall off the curve")
            }
            return PublicKey(hash)
        }

        @Throws(Exception::class)
        fun findProgramAddress(
            seeds: List<ByteArray>?,
            programId: PublicKey
        ): ProgramDerivedAddress {
            var nonce = 255
            val address: PublicKey
            val seedsWithNonce: MutableList<ByteArray> = ArrayList()
            seedsWithNonce.addAll(seeds!!)
            while (nonce != 0) {
                address = try {
                    seedsWithNonce.add(byteArrayOf(nonce.toByte()))
                    createProgramAddress(seedsWithNonce, programId)
                } catch (e: Exception) {
                    seedsWithNonce.removeAt(seedsWithNonce.size - 1)
                    nonce--
                    continue
                }
                return ProgramDerivedAddress(address, nonce)
            }
            throw Exception("Unable to find a viable program address nonce")
        }
    }
}