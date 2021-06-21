package com.solana.networking.models

import com.squareup.moshi.Json
import java.util.*

class RpcRequest (
    @Json(name = "method") val method: String,
    @Json(name = "params") val params: List<Any>? = null
) {
    @Json(name = "jsonrpc") val jsonrpc = "2.0"
    @Json(name = "id") val id = UUID.randomUUID().toString()
}
