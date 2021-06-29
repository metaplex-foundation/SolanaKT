package com.solana.programs

import com.solana.core.AccountMeta
import com.solana.core.PublicKey
import com.solana.core.TransactionInstruction

/**
 * Abstract class for
 */
abstract class Program {
    companion object {
        /**
         * Returns a [TransactionInstruction] built from the specified values.
         * @param programId Solana program we are calling
         * @param keys AccountMeta keys
         * @param data byte array sent to Solana
         * @return [TransactionInstruction] object containing specified values
         */
        @JvmStatic
        fun createTransactionInstruction(
            programId: PublicKey,
            keys: List<AccountMeta>,
            data: ByteArray
        ): TransactionInstruction {
            return TransactionInstruction(programId, keys, data)
        }
    }
}