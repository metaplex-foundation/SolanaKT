package com.solana.models

import com.squareup.moshi.Json

class StakeActivation (
    @Json(name = "active")
    private val active: Long,

    @Json(name = "inactive")
    private val inactive: Long,

    @Json(name = "state")
    private val state: String
)