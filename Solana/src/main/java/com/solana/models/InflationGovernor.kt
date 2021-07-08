package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InflationGovernor (
    val initial: Float,
    val terminal: Float,
    val taper: Float,
    val foundation: Float,
    val foundationTerm: Long
)