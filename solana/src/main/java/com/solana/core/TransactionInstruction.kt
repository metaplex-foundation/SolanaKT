package com.solana.core

class TransactionInstruction(
    var programId: PublicKey,
    var keys: List<AccountMeta>,
    var data: ByteArray
) {
    override fun toString(): String {
        return """TransactionInstruction(
            |  programId: $programId,
            |  keys: [${keys.joinToString()}],
            |  data: [${data.joinToString()}]
        |)""".trimMargin()
    }
}