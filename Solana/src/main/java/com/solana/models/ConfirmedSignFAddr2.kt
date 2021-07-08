package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfirmedSignFAddr2 (
    val limit: Long?,
    val before: String?,
    val until: String?,
)