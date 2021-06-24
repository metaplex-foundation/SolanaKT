package com.solana.models

import com.squareup.moshi.Json

enum class Encoding(private val enc: String) {
    base64("base64");
    fun getEncoding(): String {
        return enc
    }
}

class RpcSendTransactionConfig(
    @Json(name = "encoding") val encoding: Encoding = Encoding.base64,
    @Json(name = "skipPreflight") val skipPreFlight: Boolean = true
)