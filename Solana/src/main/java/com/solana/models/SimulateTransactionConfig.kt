package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SimulateTransactionConfig (
    var encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    var accounts: Map<String, *>? = null,
    val commitment: String = "finalized",
    val sigVerify: Boolean = false,
    var replaceRecentBlockhash: Boolean = false
)