package com.solana.core

class AccountMeta (
    val publicKey: PublicKey,
    val isSigner:Boolean,
    val isWritable:Boolean
)