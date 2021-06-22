package com.solana.models

import com.squareup.moshi.Json

class RpcEpochConfig(@field:Json(name = "epoch") private val epoch: Long) {
    @Json(name = "commitment")
    private val commitment: String? = null
}