package com.solana.models

import com.squareup.moshi.Json

class InflationRate {
    @Json(name = "total")
    private val total = 0f

    @Json(name = "validator")
    private val validator = 0f

    @Json(name = "foundation")
    private val foundation = 0f

    @Json(name = "epoch")
    private val epoch: Long = 0
}