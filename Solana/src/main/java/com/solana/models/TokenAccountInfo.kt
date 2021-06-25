package com.solana.models

import com.squareup.moshi.Json

class TokenAccountInfo(@Json(name = "value") val value: List<Value>? = null) : RpcResultObject() {
    class Value {
        @Json(name = "account")
        val account: TokenResultObjects.Value? = null

        @Json(name = "pubkey")
        val pubkey: String? = null
    }
}