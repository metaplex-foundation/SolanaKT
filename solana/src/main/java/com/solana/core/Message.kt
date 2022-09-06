package com.solana.core

import com.solana.vendor.borshj.BorshBuffer
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import org.bitcoinj.core.Utils

class MessageHeader {
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

    companion object {
        const val HEADER_LENGTH = 3
    }
}

data class CompiledInstruction(
    val programIdIndex: Int,
    val accounts: List<Int>,
    val data: String
)

const val PUBKEY_LENGTH = 32

class Message(
    val header: MessageHeader,
    val accountKeys: List<PublicKey>,
    val recentBlockhash: String,
    val instructions: List<com.solana.core.CompiledInstruction>
) {
    private var indexToProgramIds = mutableMapOf<Int, PublicKey>()

    init {
        this.instructions.forEach { ix ->
            this.indexToProgramIds[ix.programIdIndex] = this.accountKeys[ix.programIdIndex]
        }
    }
    fun isAccountSigner(index: Int): Boolean {
        return index < this.header.numRequiredSignatures
    }

    fun isAccountWritable(index: Int): Boolean {
        return index < header.numRequiredSignatures - header.numReadonlySignedAccounts ||
                (index >= header.numRequiredSignatures &&
                        index < accountKeys.count() - header.numReadonlyUnsignedAccounts)
    }

    fun isProgramId(index: Int): Boolean {
        return indexToProgramIds.containsKey(index)
    }

    fun programIds(): List<PublicKey> {
        return indexToProgramIds.values.toList()
    }

    fun nonProgramIds(): List<PublicKey> {
        return this.accountKeys.filterIndexed { index, _ -> !this.isProgramId(index) }
    }

    data class CompiledInstruction(
        var programIdIndex: Byte = 0,
        var keyIndicesCount: ByteArray,
        var keyIndices: ByteArray,
        var dataLength: ByteArray,
        var data: ByteArray
    ) {

        // 1 = programIdIndex length
        val length: Int
            get() =// 1 = programIdIndex length
                1 + keyIndicesCount.size + keyIndices.size + dataLength.size + data.size
    }

    private var feePayer: PublicKey? = null

    fun serialize(): ByteArray {
        require(recentBlockhash.isNotEmpty()) { "recentBlockhash required" }
        require(instructions.isNotEmpty()) { "No instructions provided" }
        val numKeys = this.accountKeys.count()
        val keyCount = Shortvec.encodeLength(numKeys)
        var compiledInstructionsLength = 0
        val instructions = this.instructions.map { instruction ->
            val (programIdIndex, accounts, _) = instruction
            val data = Base58.decode(instruction.data)

            val keyIndicesCount = Shortvec.encodeLength(accounts.count())
            val dataCount = Shortvec.encodeLength(data.count())

            CompiledInstruction(
                programIdIndex = programIdIndex.toByte(),
                keyIndicesCount = keyIndicesCount,
                keyIndices = accounts.map{ it.toByte() }.toByteArray(),
                dataLength = dataCount,
                data = data,
            )
        }
        val instructionCount = Shortvec.encodeLength(instructions.size)
        val bufferSize = (MessageHeader.HEADER_LENGTH + RECENT_BLOCK_HASH_LENGTH + keyCount.size
                + numKeys * PublicKey.PUBLIC_KEY_LENGTH + instructionCount.size
                + compiledInstructionsLength)
        val out = ByteBuffer.allocate(bufferSize)
        val accountKeysBuff = ByteBuffer.allocate(numKeys * PublicKey.PUBLIC_KEY_LENGTH)

        val buffer = BorshBuffer.allocate(2048)
        buffer.write(header.numRequiredSignatures)
        buffer.write(header.numReadonlySignedAccounts)
        buffer.write(header.numReadonlyUnsignedAccounts)
        buffer.write(keyCount)
        for (accountKey in accountKeys) {
            buffer.write(accountKey.pubkey)
        }
        buffer.write(Base58.decode(recentBlockhash))
        buffer.write(instructionCount)
        for (instruction in instructions) {
            buffer.write(instruction.programIdIndex)
            buffer.write(instruction.keyIndicesCount)
            buffer.write(instruction.keyIndices)
            buffer.write(instruction.dataLength)
            buffer.write(instruction.data)
        }
        return buffer.toByteArray()
    }

    fun setFeePayer(publicKey: PublicKey) {
        this.feePayer = publicKey
    }

    companion object {

        fun from(buffer: ByteArray): Message {
            // Slice up wire data
            var byteArray = buffer

            val numRequiredSignatures = byteArray.first().toInt().also { byteArray = byteArray.drop(1).toByteArray() }
            val numReadonlySignedAccounts = byteArray.first().toInt().also { byteArray = byteArray.drop(1).toByteArray() }
            val numReadonlyUnsignedAccounts = byteArray.first().toInt().also { byteArray = byteArray.drop(1).toByteArray() }

            val accountCount = Shortvec.decodeLength(byteArray)
            byteArray = accountCount.second
            val accountKeys = mutableListOf<String>()
            for (i in 0 until accountCount.first) {
                val account = byteArray.slice(0 until PUBKEY_LENGTH)
                byteArray = byteArray.drop(PUBKEY_LENGTH).toByteArray()
                accountKeys.add(Base58.encode(account.toByteArray()))
            }

            val recentBlockhash = byteArray.slice(0 until PUBKEY_LENGTH).toByteArray()
            byteArray = byteArray.drop(PUBKEY_LENGTH).toByteArray()

            val instructionCount = Shortvec.decodeLength(byteArray)
            byteArray = instructionCount.second
            val instructions = mutableListOf<com.solana.core.CompiledInstruction>()
            for (i in 0 until instructionCount.first) {
                val programIdIndex = byteArray.first().toInt().also { byteArray = byteArray.drop(1).toByteArray() }
                val accountCount = Shortvec.decodeLength(byteArray)
                byteArray = accountCount.second
                val accounts =
                    byteArray.slice(0 until accountCount.first).toByteArray().toList().map(Byte::toInt)
                byteArray = byteArray.drop(accountCount.first).toByteArray()
                val dataLength = Shortvec.decodeLength(byteArray)
                byteArray = dataLength.second
                val dataSlice = byteArray.slice(0 until dataLength.first).toByteArray()
                val data = Base58.encode(dataSlice)
                byteArray = byteArray.drop(dataLength.first).toByteArray()
                instructions.add(
                    CompiledInstruction(
                        programIdIndex = programIdIndex,
                        accounts = accounts,
                        data = data,
                    )
                )
            }

            return Message(
                header = MessageHeader().apply {
                    this.numRequiredSignatures = numRequiredSignatures.toByte()
                    this.numReadonlySignedAccounts = numReadonlySignedAccounts.toByte()
                    this.numReadonlyUnsignedAccounts = numReadonlyUnsignedAccounts.toByte()
                },
                accountKeys = accountKeys.map { PublicKey(it) },
                recentBlockhash = String().plus(Base58.encode(recentBlockhash)),
                instructions = instructions
            )
        }

        private const val RECENT_BLOCK_HASH_LENGTH = 32
    }
}

object Shortvec {
    @JvmStatic
    fun decodeLength(bytes: ByteArray): Pair<Int, ByteArray> {
        var newBytes = bytes
        var len = 0
        var size = 0
        while (true) {
            val elem = newBytes.first().toInt().also { newBytes = newBytes.drop(1).toByteArray() }
            len = len or (elem and 0x7f) shl (size * 7)
            size += 1
            if ((elem and 0x80) == 0) {
                break
            }
        }
        return len to newBytes
    }

    @JvmStatic
    fun encodeLength(len: Int): ByteArray {
        val out = ByteArray(10)
        var remLen = len
        var cursor = 0
        while (true) {
            var elem = remLen and 0x7f
            remLen = remLen shr 7
            if (remLen == 0) {
                Utils.uint16ToByteArrayLE(elem, out, cursor)
                break
            } else {
                elem = elem or 0x80
                Utils.uint16ToByteArrayLE(elem, out, cursor)
                cursor += 1
            }
        }
        val bytes = ByteArray(cursor + 1)
        System.arraycopy(out, 0, bytes, 0, cursor + 1)
        return bytes
    }

}