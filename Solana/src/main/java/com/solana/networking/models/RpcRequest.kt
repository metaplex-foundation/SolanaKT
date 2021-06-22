package com.solana.networking.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class RpcRequest (
    @Json(name = "method") val method: String,
    @Json(name = "params") val params: List<Any>? = null,
    @Json(name = "jsonrpc") val jsonrpc :String  = "2.0",
    @Json(name = "id") val id: String = UUID.randomUUID().toString()
)
