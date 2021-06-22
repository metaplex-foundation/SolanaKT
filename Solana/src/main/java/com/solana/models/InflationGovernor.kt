package com.solana.models

import com.squareup.moshi.Json

class InflationGovernor {
    @Json(name = "initial")
    private val initial = 0f

    @Json(name = "terminal")
    private val terminal = 0f

    @Json(name = "taper")
    private val taper = 0f

    @Json(name = "foundation")
    private val foundation = 0f

    @Json(name = "foundationTerm")
    private val foundationTerm: Long = 0
}