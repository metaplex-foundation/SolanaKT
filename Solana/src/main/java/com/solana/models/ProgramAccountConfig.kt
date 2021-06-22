package com.solana.models

import com.squareup.moshi.Json

class ProgramAccountConfig {
    @Json(name = "encoding")
    private var encoding: RpcSendTransactionConfig.Encoding? = null

    @Json(name = "filters")
    private var filters: List<Any>? = null

    @Json(name = "commitment")
    private val commitment = "processed"

    constructor(filters: List<Any>?) {
        this.filters = filters
    }

    constructor(encoding: RpcSendTransactionConfig.Encoding?) {
        this.encoding = encoding
    }
}