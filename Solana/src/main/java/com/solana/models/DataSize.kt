package com.solana.models

import com.squareup.moshi.Json

data class DataSize (
    @Json(name = "dataSize")  val dataSize: Long
)