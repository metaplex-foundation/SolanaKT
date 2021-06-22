package com.solana.models

import com.squareup.moshi.Json

class SimulateTransactionConfig {
    @Json(name = "encoding")
    private var encoding = RpcSendTransactionConfig.Encoding.base64

    @Json(name = "accounts")
    private var accounts: Map<*, *>? = null

    @Json(name = "commitment")
    private val commitment = "finalized"

    @Json(name = "sigVerify")
    private val sigVerify = false

    @Json(name = "replaceRecentBlockhash")
    private val replaceRecentBlockhash = false

    constructor(accounts: Map<*, *>?) {
        this.accounts = accounts
    }

    constructor(encoding: RpcSendTransactionConfig.Encoding) {
        this.encoding = encoding
    }
}