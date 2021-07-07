package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Supply(
    override val value: Value
) : RPC<Supply.Value>(null, value) {
    @JsonClass(generateAdapter = true)
    class Value (
        val total: Long,
        val circulating: Long,
        val nonCirculating: Long,
        val nonCirculatingAccounts: List<String>
    )
}