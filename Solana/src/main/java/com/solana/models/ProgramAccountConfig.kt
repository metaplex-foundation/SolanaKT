package com.solana.models

import com.squareup.moshi.Json

class ProgramAccountConfig(
    @Json(name = "encoding") var encoding: Encoding,
    @Json(name = "filters")  var filters: List<Any>? = null,
    @Json(name = "commitment") private val commitment: String = "processed"
)