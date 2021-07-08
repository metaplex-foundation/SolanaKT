package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StakeActivation (
    val active: Long,
    val inactive: Long,

    val state: String
)