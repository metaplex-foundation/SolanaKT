package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProgramAccountConfig(
    var encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    var filters: List<Any>? = null,
    var dataSlice: DataSlice? = null,
    val commitment: String = "processed"
)

@JsonClass(generateAdapter = true)
data class DataSlice(val offset: Int, val length: Int)