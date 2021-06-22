package com.solana.core

class TransactionInstruction (
    val programId: PublicKey,
    val keys: List<AccountMeta>,
    val data: ByteArray
)