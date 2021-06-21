package com.solana.models

class TransactionInstruction(
    val programId: PublicKey,
    val keys: List<AccountMeta>,
    val data: ByteArray
)