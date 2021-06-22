package com.solana.models

import com.squareup.moshi.Json

class Memcmp {
    @Json(name = "offset")
    private val offset: Long = 0

    @Json(name = "bytes")
    private val bytes: String? = null
}