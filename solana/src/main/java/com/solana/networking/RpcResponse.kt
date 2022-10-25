/*
 * RpcResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.solana.networking

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

typealias DefaultRpcResponse = RpcResponse<JsonElement>

@Serializable
data class RpcError(val code: Int, val message: String)

@Serializable
open class RpcResponse<R>(
    val result: R? = null,
    val error: RpcError? = null,
    val id: String? = null
) {
    val jsonrpc = "2.0"
}

@Serializable
data class Context (val slot: Long)

@Serializable
data class  RPC<R>(
    val context: Context?,
    val value: R? = null
)

@Serializable
data class Params<R> (
    val result: RPC<R>?,
    val subscription: Int
)

