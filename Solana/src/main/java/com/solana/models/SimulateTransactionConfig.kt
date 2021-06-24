package com.solana.models

import com.squareup.moshi.Json

class SimulateTransactionConfig {
    @Json(name = "encoding")
    var encoding = RpcSendTransactionConfig.Encoding.base64

    @Json(name = "accounts")
    var accounts: Map<*, *>? = null

    @Json(name = "commitment")
    val commitment: String = "finalized"

    @Json(name = "sigVerify")
    val sigVerify: Boolean = false

    @Json(name = "replaceRecentBlockhash")
    var replaceRecentBlockhash: Boolean = false


    constructor(encoding: RpcSendTransactionConfig.Encoding) {
        this.encoding = encoding
    }
}