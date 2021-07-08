package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RpcSendTransactionConfig(
    val encoding: Encoding = Encoding.base64,
    val skipPreFlight: Boolean = true
){
    enum class Encoding(private val enc: String) {
        base64("base64"),
        base58("base58");
        fun getEncoding(): String {
            return enc
        }
    }
}