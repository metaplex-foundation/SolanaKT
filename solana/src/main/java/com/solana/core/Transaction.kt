package com.solana.core

import com.solana.vendor.ShortvecEncoding
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import java.util.*

class Transaction {
    private val message: Message = Message()
    private val signatures: MutableList<String> = ArrayList()
    private lateinit var serializedMessage: ByteArray
    fun addInstruction(instruction: TransactionInstruction): Transaction {
        message.addInstruction(instruction)
        return this
    }

    fun setRecentBlockHash(recentBlockhash: String) {
        message.setRecentBlockHash(recentBlockhash)
    }

    fun setFeePayer(feePayer: PublicKey?) {
        message.feePayer = feePayer
    }

    fun sign(signer: Account) {
        sign(listOf(signer))
    }

    fun sign(signers: List<Account>) {
        require(signers.size != 0) { "No signers" }
        // Fee payer defaults to first signer if not set
        message.feePayer ?: let {
            message.feePayer = signers[0].publicKey
        }
        serializedMessage = message.serialize()
        for (signer in signers) {
            val signatureProvider = TweetNaclFast.Signature(ByteArray(0), signer.secretKey)
            val signature = signatureProvider.detached(serializedMessage)
            signatures.add(Base58.encode(signature))
        }
    }

    fun serialize(): ByteArray {
        val signaturesSize = signatures.size
        val signaturesLength = ShortvecEncoding.encodeLength(signaturesSize)
        val out = ByteBuffer
            .allocate(signaturesLength.size + signaturesSize * SIGNATURE_LENGTH + serializedMessage.size)
        out.put(signaturesLength)
        for (signature in signatures) {
            val rawSignature = Base58.decode(signature)
            out.put(rawSignature)
        }
        out.put(serializedMessage)
        return out.array()
    }

    override fun toString(): String {
        return """Transaction(
            | signatures: [${signatures.joinToString()}],
            | message: ${message}
        )""".trimMargin()
    }

    companion object {
        const val SIGNATURE_LENGTH = 64
    }
}
