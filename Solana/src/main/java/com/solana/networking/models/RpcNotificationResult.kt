package com.solana.networking.models

import com.solana.models.RpcResultObject
import com.squareup.moshi.Json

class RpcNotificationResult {
    class Result : RpcResultObject() {
        @Json(name = "value")
        val value: Any? = null
    }

    class Params {
        @Json(name = "result")
        val result: Result? = null

        @Json(name = "subscription")
        val subscription: Long = 0
    }

    @Json(name = "jsonrpc")
    val jsonrpc: String? = null

    @Json(name = "method")
    val method: String? = null

    @Json(name = "params")
    val params: Params? = null
}