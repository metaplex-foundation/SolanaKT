package com.solana.models

import com.squareup.moshi.Json

class ProgramAccountConfig(
    @Json(name = "encoding") var encoding: RpcSendTransactionConfig.Encoding = RpcSendTransactionConfig.Encoding.base64,
    @Json(name = "filters")  var filters: List<Any>? = null,
    @Json(name = "commitment") val commitment: String = "processed"
)