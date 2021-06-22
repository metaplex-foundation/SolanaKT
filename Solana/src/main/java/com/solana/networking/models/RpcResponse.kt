package com.solana.networking.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RPCError(
    @Json(name = "code") val code: Long,
    @Json(name = "message") val message: String?
)

@JsonClass(generateAdapter = true)
class RpcResponse<T>(
    @Json(name = "error") val error: RPCError?,
    @Json(name = "jsonrpc") val jsonrpc: String?,
    @Json(name = "id") val id: String?,
    @Json(name = "result") val result: T?
)
