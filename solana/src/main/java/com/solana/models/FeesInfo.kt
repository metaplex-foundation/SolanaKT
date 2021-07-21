package com.solana.models

import com.solana.models.RecentBlockhash.FeeCalculator
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeesInfo(
    override val value: Value
) : RPC<FeesInfo.Value>(null, value){
    @JsonClass(generateAdapter = true)
    class Value (
        val blockhash: String,
        val feeCalculator: FeeCalculator,
        val lastValidSlot: Long,
        val lastValidBlockHeight: Long
    )
}