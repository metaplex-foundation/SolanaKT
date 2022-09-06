package com.solana.core

class TransactionBuilder {
    private val transaction: Transaction = Transaction()
    fun addInstruction(transactionInstruction: TransactionInstruction): TransactionBuilder {
        transaction.add(transactionInstruction)
        return this
    }

    fun setRecentBlockHash(recentBlockHash: String): TransactionBuilder {
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
}