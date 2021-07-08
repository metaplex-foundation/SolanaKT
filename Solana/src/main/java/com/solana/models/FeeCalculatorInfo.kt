package com.solana.models

import com.solana.models.RecentBlockhash.FeeCalculator
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeeCalculatorInfo(
    override val value: Value?
) : RPC<FeeCalculatorInfo.Value>(null, value){
    @JsonClass(generateAdapter = true)
    data class Value (val feeCalculator: FeeCalculator)
}