package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InflationRate (
    val total:Float,
    val validator:Float,
    val foundation:Float,
    val epoch: Long = 0
)