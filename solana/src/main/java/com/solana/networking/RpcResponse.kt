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

interface JsonRpcResponse<R> {
    val result: R?
    val error: RpcError?
    val id: String?
}

@Serializable
data class RpcResponse<R>(
    override val result: R? = null,
    override val error: RpcError? = null,
    override val id: String? = null
): JsonRpcResponse<R> {
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

