package com.solana.models

import com.solana.models.buffer.BufferInfo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TokenAccountInfo(
    override val value: List<Value>? = null
) : RPC<List<TokenAccountInfo.Value>>(null, value) {
    @JsonClass(generateAdapter = true)
    class Value(
        val account: BufferInfo<TokenResultObjects.Data>? = null,
        val pubkey: String? = null
    )
}