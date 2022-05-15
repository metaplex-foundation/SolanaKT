package com.solana.core

import com.solana.vendor.ShortvecEncoding
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import java.util.*

class Message {
    private class MessageHeader {
        var numRequiredSignatures: Byte = 0
        var numReadonlySignedAccounts: Byte = 0
        var numReadonlyUnsignedAccounts: Byte = 0
        fun toByteArray(): ByteArray {
            return byteArrayOf(
                numRequiredSignatures,
                numReadonlySignedAccounts,
                numReadonlyUnsignedAccounts
            )
        }

        override fun toString(): String {
            return "numRequiredSignatures: $numRequiredSignatures, numReadOnlySignedAccounts: $numReadonlySignedAccounts, numReadOnlyUnsignedAccounts: $numReadonlyUnsignedAccounts"
        }

        companion object {
            const val HEADER_LENGTH = 3
        }
    }

    private class CompiledInstruction {
        var programIdIndex: Byte = 0
        lateinit var keyIndicesCount: ByteArray
        lateinit var keyIndices: ByteArray
        lateinit var dataLength: ByteArray
        lateinit var data: ByteArray

        // 1 = programIdIndex length
        val length: Int
            get() =// 1 = programIdIndex length
                1 + keyIndicesCount.size + keyIndices.size + dataLength.size + data.size
    }

    private var recentBlockhash: String? = null
    private val accountKeys: AccountKeysList = AccountKeysList()
    private val instructions: MutableList<TransactionInstruction>
    var feePayer: PublicKey? = null

    fun addInstruction(instruction: TransactionInstruction): Message {
        accountKeys.addAll(instruction.keys)
        accountKeys.add(AccountMeta(instruction.programId, false, false))
        instructions.add(instruction)
        return this
    }

    fun setRecentBlockHash(recentBlockhash: String?) {
        this.recentBlockhash = recentBlockhash
    }

    fun serialize(): ByteArray {
        requireNotNull(recentBlockhash) { "recentBlockhash required" }
        require(instructions.size != 0) { "No instructions provided" }
        val messageHeader = MessageHeader()
        val keysList = getAccountKeys()
        val accountKeysSize = keysList.size
        val accountAddressesLength = ShortvecEncoding.encodeLength(accountKeysSize)
        var compiledInstructionsLength = 0
        val compiledInstructions: MutableList<CompiledInstruction> = ArrayList()
        for (instruction in instructions) {
            val keysSize = instruction.keys.size
            val keyIndices = ByteArray(keysSize)
            for (i in 0 until keysSize) {
                keyIndices[i] = findAccountIndex(keysList, instruction.keys[i].publicKey).toByte()
            }
            val compiledInstruction = CompiledInstruction()
            compiledInstruction.programIdIndex =
                findAccountIndex(keysList, instruction.programId).toByte()
            compiledInstruction.keyIndicesCount = ShortvecEncoding.encodeLength(keysSize)
            compiledInstruction.keyIndices = keyIndices
            compiledInstruction.dataLength = ShortvecEncoding.encodeLength(instruction.data.count())
            compiledInstruction.data = instruction.data
            compiledInstructions.add(compiledInstruction)
            compiledInstructionsLength += compiledInstruction.length
        }
        val instructionsLength = ShortvecEncoding.encodeLength(compiledInstructions.size)
        val bufferSize =
            (MessageHeader.HEADER_LENGTH + RECENT_BLOCK_HASH_LENGTH + accountAddressesLength.size
                    + accountKeysSize * PublicKey.PUBLIC_KEY_LENGTH + instructionsLength.size
                    + compiledInstructionsLength)
        val out = ByteBuffer.allocate(bufferSize)
        val accountKeysBuff = ByteBuffer.allocate(accountKeysSize * PublicKey.PUBLIC_KEY_LENGTH)
        for (accountMeta in keysList) {
            accountKeysBuff.put(accountMeta.publicKey.toByteArray())
            if (accountMeta.isSigner) {
                messageHeader.numRequiredSignatures = (messageHeader.numRequiredSignatures.plus(1)).toByte()
                if (!accountMeta.isWritable) {
                    messageHeader.numReadonlySignedAccounts = (messageHeader.numReadonlySignedAccounts.plus(1)).toByte()
                }
            } else {
                if (!accountMeta.isWritable) {
                    messageHeader.numReadonlyUnsignedAccounts= (messageHeader.numReadonlyUnsignedAccounts.plus(1)).toByte()
                }
            }
        }
        out.put(messageHeader.toByteArray())
        out.put(accountAddressesLength)
        out.put(accountKeysBuff.array())
        out.put(Base58.decode(recentBlockhash))
        out.put(instructionsLength)
        for (compiledInstruction in compiledInstructions) {
            out.put(compiledInstruction.programIdIndex)
            out.put(compiledInstruction.keyIndicesCount)
            out.put(compiledInstruction.keyIndices)
            out.put(compiledInstruction.dataLength)
            out.put(compiledInstruction.data)
        }
        return out.array()
    }

    private fun getAccountKeys(): List<AccountMeta> {
        val keysList: MutableList<AccountMeta> = accountKeys.list
        val newList: MutableList<AccountMeta> = ArrayList()
        try {
            val feePayerIndex = findAccountIndex(keysList, feePayer!!)
            val feePayerMeta = keysList[feePayerIndex]
            newList.add(AccountMeta(feePayerMeta.publicKey, true, true))
            keysList.removeAt(feePayerIndex)
        } catch(e: RuntimeException) { // Fee payer not yet in list
            newList.add(AccountMeta(feePayer!!, true, true))
        }
        newList.addAll(keysList)
        return newList
    }

    private fun findAccountIndex(accountMetaList: List<AccountMeta>, key: PublicKey): Int {
        for (i in accountMetaList.indices) {
            if (accountMetaList[i].publicKey.equals(key)) {
                return i
            }
        }
        throw RuntimeException("unable to find account index")
    }

    override fun toString(): String {
        return """Message(
            |  header: not set,
            |  accountKeys: [${accountKeys.list.joinToString()}],
            |  recentBlockhash: $recentBlockhash,
            |  instructions: [${instructions.joinToString()}]
        |)""".trimMargin()
    }

    companion object {
        private const val RECENT_BLOCK_HASH_LENGTH = 32
    }

    init {
        instructions = ArrayList()
    }
}