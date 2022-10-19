package com.solana.api

import com.solana.core.Account
import com.solana.core.Transaction
import com.solana.models.RpcSendTransactionConfig
import java.util.*

fun Api.sendTransaction(
    transaction: Transaction,
    signers: List<Account>,
    recentBlockHash: String? = null,
    onComplete: ((Result<String>) -> Unit)
) {
    if (recentBlockHash == null) {
        getRecentBlockhash { result ->
            result.map { recentBlockHash ->
                transaction.setRecentBlockHash(recentBlockHash)
                transaction.sign(signers)
                val serialized = transaction.serialize()
                sendRawTransaction(serialized, onComplete = onComplete)
            }
        }
    } else {
        transaction.setRecentBlockHash(recentBlockHash)
        transaction.sign(signers)
        val serialized = transaction.serialize()

        sendRawTransaction(serialized, onComplete = onComplete)
    }
}