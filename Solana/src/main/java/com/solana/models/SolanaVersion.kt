package com.solana.models

import com.squareup.moshi.Json

class SolanaVersion {
    @Json(name = "solana-core")
    private val solanaCore: String? = null

    @Json(name = "feature-set")
    private val featureSet: String? = null
}