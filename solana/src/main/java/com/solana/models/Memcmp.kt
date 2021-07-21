package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Memcmp(
    val offset: Long,
    val bytes: String
)