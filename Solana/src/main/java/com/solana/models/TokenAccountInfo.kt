package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TokenAccountInfo(
    override val value: List<Value>? = null
) : RPC<List<TokenAccountInfo.Value>>(null, value) {
    @JsonClass(generateAdapter = true)
    class Value(
        val account: TokenResultObjects.Value? = null,
        val pubkey: String? = null
    )
}