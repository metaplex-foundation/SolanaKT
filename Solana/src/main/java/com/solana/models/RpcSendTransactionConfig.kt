package com.solana.models

import com.squareup.moshi.Json

class RpcSendTransactionConfig {
    enum class Encoding(private val enc: String) {
        base64("base64");

        fun getEncoding(): String {
            return enc
        }
    }

    @Json(name = "encoding")
    private val encoding = Encoding.base64
}