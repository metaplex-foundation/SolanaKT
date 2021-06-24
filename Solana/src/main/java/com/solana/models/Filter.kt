package com.solana.models

import com.squareup.moshi.Json

class Filter (
    @Json(name = "memcmp") val memcmp: Memcmp
)