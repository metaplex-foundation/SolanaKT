package com.solana.core

/**
 * Builder for constructing [Transaction] objects to be used in sendTransaction.
 */
class TransactionBuilder {
    private val transaction: Transaction
    fun addInstruction(transactionInstruction: TransactionInstruction?): TransactionBuilder {
        transaction.addInstruction(transactionInstruction)
        return this
    }

    fun setRecentBlockHash(recentBlockHash: String?): TransactionBuilder {
        transaction.setRecentBlockHash(recentBlockHash)
        return this
    }

    fun setSigners(signers: List<Account>): TransactionBuilder {
        transaction.sign(signers)
        return this
    }

    fun build(): Transaction {
        return transaction
    }

    init {
        transaction = Transaction()
    }
}