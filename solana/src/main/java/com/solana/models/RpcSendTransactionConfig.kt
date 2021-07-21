package com.solana.models

import com.squareup.moshi.Json

class RpcSendTransactionConfig(
    @Json(name = "encoding") val encoding: String = Encoding.base64.getEncoding(),
){
    enum class Encoding(private val enc: String) {
        base64("base64"),
        base58("base58");
        fun getEncoding(): String {
            return enc
        }
    }
}