package com.solana.actions

import com.solana.api.sendTransaction
import com.solana.core.Account
import com.solana.core.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Action.serializeAndSendWithFee(transaction: Transaction,
                                   signers: List<Account>,
                                   recentBlockHash: String? = null,
                                   onComplete: ((Result<String>) -> Unit)
) {
    CoroutineScope(api.dispatcher).launch {
        onComplete(serializeAndSendWithFee(transaction, signers, recentBlockHash))
    }
}

suspend fun Action.serializeAndSendWithFee(
    transaction: Transaction,
    signers: List<Account>,
    recentBlockHash: String? = null
): Result<String> = api.sendTransaction(transaction, signers, recentBlockHash)