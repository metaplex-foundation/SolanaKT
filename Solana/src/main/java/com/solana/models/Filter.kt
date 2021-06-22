package com.solana.models

import com.squareup.moshi.Json

class Filter {
    @Json(name = "memcmp")
    private val memcmp: Memcmp? = null
}