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
open class RpcRequestSerializable {
    open val method: String = ""
    open val params: JsonElement? = null
    val jsonrpc = "2.0"
    val id: String = UUID.randomUUID().toString()
}
