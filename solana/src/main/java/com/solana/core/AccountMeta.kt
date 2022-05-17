package com.solana.core

class AccountMeta(var publicKey: PublicKey, var isSigner: Boolean, var isWritable: Boolean) {
    override fun toString(): String {
        return "AccountMeta(publicKey: $publicKey, isSigner: $isSigner, isWritable: $isWritable)"
    }
}