/*
 * RpcResponse
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.solana.networking

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

typealias DefaultRpcResponse = RpcResponseSerializable<JsonElement>

@Serializable
data class RpcError(val code: Int, val message: String)

@Serializable
data class RpcResponseSerializable<R>(
    val result: R? = null,
    val error: RpcError? = null,
    val id: String? = null,

    // Sockets
    val params: Params<R>? = null
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

