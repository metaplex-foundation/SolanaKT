package com.solana.models

import com.squareup.moshi.Json

class TokenAccountInfo : RpcResultObject() {
    class Value {
        @Json(name = "account")
        private val account: TokenResultObjects.Value? = null

        @Json(name = "pubkey")
        private val pubkey: String? = null
    }

    @Json(name = "value")
    private val value: List<Value>? = null
}