package com.solana.api

import com.solana.core.PublicKey
import com.solana.models.RpcSendTransactionConfig
import com.solana.models.SimulateTransactionConfig
import com.solana.models.SimulatedTransaction

fun Api.simulateTransaction(
    transaction: String,
    addresses: List<PublicKey>,
    onComplete: (Result<SimulatedTransaction>) -> Unit
) {
    val simulateTransactionConfig =
        SimulateTransactionConfig(RpcSendTransactionConfig.Encoding.base64)
    val base58addresses = addresses.map(PublicKey::toBase58)
    val accounts = mapOf(
        "encoding" to RpcSendTransactionConfig.Encoding.base64.getEncoding(),
        "addresses" to base58addresses)
    simulateTransactionConfig.accounts = accounts
    simulateTransactionConfig.replaceRecentBlockhash = true
    val params: MutableList<Any> = ArrayList()
    params.add(transaction)
    params.add(simulateTransactionConfig)
    router.call(
        "simulateTransaction",
        params,
        SimulatedTransaction::class.java,
        onComplete
    )
}