/*
 * RpcRequest
 * Metaplex
 * 
 * Created by Funkatronics on 7/27/2022
 */

package com.solana.networking

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

@Serializable
open class RpcRequest (
    open val method: String = "",
    open val params: JsonElement? = null,
    val jsonrpc: String = "2.0",
    val id: String = UUID.randomUUID().toString()
)
