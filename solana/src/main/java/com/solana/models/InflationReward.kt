package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InflationReward(
    val epoch: Double,
    val effectiveSlot: Double,
    val amount: Double,
    val postBalance: Double
)