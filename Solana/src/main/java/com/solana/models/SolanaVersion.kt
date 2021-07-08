package com.solana.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SolanaVersion (
    @Json(name = "solana-core")
    val solanaCore: String,
    @Json(name = "feature-set")
    val featureSet: String
)