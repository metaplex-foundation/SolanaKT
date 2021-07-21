package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProgramAccountConfig(
    var encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    var filters: List<Any>? = null,
    val commitment: String = "processed"
)