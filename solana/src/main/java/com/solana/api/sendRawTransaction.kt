package com.solana.api

import com.solana.models.RpcSendTransactionConfig
import java.util.*

/**
 * Send a transaction that has already been signed and serialized into the
 * wire format
 */
fun Api.sendRawTransaction(
    transaction: ByteArray, config: RpcSendTransactionConfig = RpcSendTransactionConfig(),
    onComplete: (Result<String>) -> Unit
) {
    val base64Encoded = Base64.getEncoder().encodeToString(transaction)
    router.request(
        "sendTransaction",
        listOf(base64Encoded, config),
        String::class.java,
        onComplete
    )
}