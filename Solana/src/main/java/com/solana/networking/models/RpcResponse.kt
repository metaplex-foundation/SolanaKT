package com.solana.networking.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RPCError(
    val code: Long,
    val message: String?
)

@JsonClass(generateAdapter = true)
class RpcResponse<T>(
    val error: RPCError?,
    val jsonrpc: String?,
    val id: String?,
    val result: T?
)
