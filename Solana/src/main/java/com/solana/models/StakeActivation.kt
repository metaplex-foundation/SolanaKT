package com.solana.models

import com.squareup.moshi.Json

class StakeActivation {
    @Json(name = "active")
    private val active: Long = 0

    @Json(name = "inactive")
    private val inactive: Long = 0

    @Json(name = "state")
    private val state: String? = null
}