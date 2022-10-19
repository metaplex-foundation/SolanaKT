package com.solana.networking.models

import com.solana.models.RPC
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.*

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
    val result: T?,

    // Socket
    val params: Params<T>?
)

class Params<T> (
    val result: RPC<T>?,
    val subscription: Int
)