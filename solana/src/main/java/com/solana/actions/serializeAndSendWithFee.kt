package com.solana.actions

import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.Transaction

fun Action.serializeAndSendWithFee(transaction: Transaction,
                                   signers: List<Account>,
                                   recentBlockHash: String? = null,
                                   onComplete: ((Result<String>) -> Unit)
) {
    api.sendTransaction(transaction, signers, recentBlockHash, onComplete)
}