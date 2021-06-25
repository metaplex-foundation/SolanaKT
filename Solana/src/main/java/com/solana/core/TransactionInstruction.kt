package com.solana.core

class TransactionInstruction(
    var programId: PublicKey,
    var keys: List<AccountMeta>,
    var data: ByteArray
)