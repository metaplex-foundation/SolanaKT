package com.solana.models

import com.squareup.moshi.Json

class SplTokenAccountInfo : RpcResultObject() {
    @Json(name = "value")
    private val value: TokenResultObjects.Value? = null
}