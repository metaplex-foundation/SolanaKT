package com.solana.core

import com.solana.vendor.*
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import java.util.*

class Transaction {
    private val message: Message = Message()
    private var signatures: MutableList<Signature> = ArrayList()
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

    fun sign(signer: Account): Result<Unit, ResultError> {
        return sign(listOf(signer))
    }

    fun sign(signers: List<Account>): Result<Unit, ResultError> {
        require(signers.isNotEmpty()) { return Result.failure(ResultError("No signers")) }
        // Fee payer defaults to first signer if not set
        message.feePayer ?: let {
            message.feePayer = signers[0].publicKey
        }
        serializedMessage = message.serialize()
        for (signer in signers) {
            val signatureProvider = TweetNaclFast.Signature(ByteArray(0), signer.secretKey)
            val signature = signatureProvider.detached(serializedMessage)
            signatures.add(Signature(signature, signer.publicKey))
            //addSignature(Signature(signature, signer.publicKey))
        }
        return Result.success(Unit)
    }

    fun serialize(
        requiredAllSignatures: Boolean = true,
        verifySignatures: Boolean = false
    ): Result<ByteArray, ResultError> {

        val signaturesSize = signatures.size
        val signaturesLength = ShortvecEncoding.encodeLength(signaturesSize)
        val out = ByteBuffer
            .allocate(signaturesLength.size + signaturesSize * SIGNATURE_LENGTH + serializedMessage.size)
        out.put(signaturesLength)
        for (signature in signatures) {
            signature.signature?.let { out.put(it) }
        }
        out.put(serializedMessage)
        if(verifySignatures) {
            if(_verifySignatures(requiredAllSignatures).getOrDefault(false)){
                return Result.failure(ResultError("Could not verify"))
            }
        }
        return Result.success(out.array())
    }

    // MARK: - Helpers

    fun addSignature(signature: Signature): Result<Unit, ResultError> {
        return compile() // Ensure signatures array is populated
            .flatMap { _addSignature(signature) }
    }

    fun serializeMessage(): Result<ByteArray, ResultError> {
        return compile()
            .map { it.serialize() }
    }

    fun findSignature(pubkey: PublicKey) = signatures.firstOrNull { it.publicKey == pubkey }

    // MARK: - Compiling

    fun compile(): Result<Message, ResultError> {
        return compileMessage().map { message ->
            val signedKeys = message.accountKeys.list
            if(signatures.size == signedKeys.size){
                var isValid = true
                for ((index, signature) in signatures.withIndex()) {
                    if(signedKeys[index].publicKey != signature.publicKey) {
                        isValid = false
                        break
                    }
                }
                if(isValid){
                    return@map message
                }
            }
            signatures = signedKeys.map { Signature(null, it.publicKey) }.toMutableList()
            message
        }
    }

    fun compileMessage(): Result<Message, ResultError> {
        require(message.instructions.size > 0) { Result.failure(ResultError("No instructions provided")) }
        val programIds = mutableListOf<PublicKey>()
        val accountMetas = mutableListOf<AccountMeta>()

        for (instruction in message.instructions) {
            accountMetas.addAll(instruction.keys)
            if(!programIds.contains(instruction.programId)){
                programIds.add(instruction.programId)
            }
        }
        for(programId in programIds){
            accountMetas.add(
                AccountMeta(programId, isSigner = false, isWritable = false)
            )
        }

        val comparator = Comparator { x: AccountMeta, y: AccountMeta ->
            if(x.isSigner != y.isSigner) { return@Comparator x.isSigner.compareTo(true) }
            if(x.isWritable != y.isWritable) { return@Comparator x.isWritable.compareTo(true) }
            return@Comparator false.compareTo(false)
        }

        accountMetas.sortWith(comparator)

        accountMetas.fold(listOf<AccountMeta>()) { result, accountMeta ->
            val uniqueMetas = result.toMutableList()
            val index = uniqueMetas.indexOfFirst { it.publicKey == accountMeta.publicKey }
            if(index >= 0) {
                uniqueMetas[index].isWritable = uniqueMetas[index].isWritable || accountMeta.isWritable
            } else {
                uniqueMetas.add(accountMeta)
            }
            uniqueMetas
        }
        accountMetas.removeAll { it.publicKey == message.feePayer}
        return Result.success(message)
    }

    fun _partialSign(message: Message, signers: List<Account>): Result<Unit, ResultError> {
        require(signers.isNotEmpty()) { return Result.failure(ResultError("No signers")) }
        serializedMessage = message.serialize()
        for (signer in signers) {
            val signatureProvider = TweetNaclFast.Signature(ByteArray(0), signer.secretKey)
            val signature = signatureProvider.detached(serializedMessage)
            _addSignature(Signature(signature, signer.publicKey))
        }
        return Result.success(Unit)
    }

    private fun _addSignature(signature: Signature): Result<Unit, ResultError> {
        val data = signature.signature
        require(data?.size == 64) { return Result.failure("Signer not valid ${signature.publicKey.toBase58()}") }
        val index = signatures.indexOfFirst { it.publicKey == signature.publicKey }
        return if (index >= 0) {
            signatures[index] = signature
            Result.success(Unit)
        } else {
            Result.failure("Signer not valid ${signature.publicKey.toBase58()}")
        }
    }

    // MARK: - Verifying
    private fun _verifySignatures(
        requiredAllSignatures: Boolean
    ): Result<Boolean, ResultError> {
        for (signature in signatures) {
            if (signature.signature == null) {
                if (requiredAllSignatures) {
                    return Result.success(false)
                }
            } else {
                if (!TweetNaclFast.Signature(signature.publicKey.toByteArray(), ByteArray(0))
                        .detached_verify(serializedMessage, signature.signature)
                ) {
                    return Result.success(false)
                }
            }
        }
        return Result.success(true)
    }

    override fun toString(): String {
        return """Transaction(
            |  signatures: [${signatures.joinToString()}],
            |  message: ${message}
        |)""".trimMargin()
    }

    companion object {
        const val SIGNATURE_LENGTH = 64
    }

    data class Signature(
        val signature: ByteArray?,
        val publicKey: PublicKey
    ) {
        override fun toString(): String {
            return "${publicKey.toBase58()} -> ${signature?.let { Base58.encode(it) }}"
        }
    }
}
