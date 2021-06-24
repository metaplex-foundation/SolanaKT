package com.solana.models

import com.squareup.moshi.Json

class Memcmp(
    @Json(name = "offset") val offset: Long,
    @Json(name = "bytes") val bytes: String
)