package com.solana.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataSize (
    val dataSize: Long
)