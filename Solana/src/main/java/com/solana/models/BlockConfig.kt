package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockConfig (
    val encoding: String = "json",
    val transactionDetails: String = "full",
    val rewards: Boolean = true,
    val commitment: String = "finalized"
)