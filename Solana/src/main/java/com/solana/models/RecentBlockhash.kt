package com.solana.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RecentBlockhash(
    override val value: Value
) : RpcResultObject<RecentBlockhash.Value>(null, value) {
    @JsonClass(generateAdapter = true)
    class FeeCalculator (
        val lamportsPerSignature: Long = 0
    )

    @JsonClass(generateAdapter = true)
    class Value (
        val blockhash: String,
        val feeCalculator: FeeCalculator
    )
}