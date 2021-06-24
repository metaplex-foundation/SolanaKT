package com.solana.models

import com.squareup.moshi.Json

class ConfirmedSignFAddr2 (
    @Json(name = "limit") val limit: Long?,
    @Json(name = "before") val before: String?,
    @Json(name = "until") val until: String?,
)