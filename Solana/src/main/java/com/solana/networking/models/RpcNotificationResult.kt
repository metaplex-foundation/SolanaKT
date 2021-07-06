package com.solana.networking.models

import com.solana.models.RpcResultObject
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class RpcNotificationResult {

    @JsonClass(generateAdapter = true)
    class Result(
        override val value: Any? = null
    ) : RpcResultObject<Any>(null, value)

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