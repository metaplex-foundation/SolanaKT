package com.solana.models

data class ProgramAccountConfig(
    var encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    var filters: List<Any>? = null,
    var dataSlice: DataSlice? = null,
    val commitment: String = "processed"
)

data class DataSlice(val offset: Int, val length: Int)